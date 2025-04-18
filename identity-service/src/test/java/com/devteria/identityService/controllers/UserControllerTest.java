package com.devteria.identityService.controllers;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.devteria.identityService.dto.request.UserCreationRequest;
import com.devteria.identityService.dto.response.UserResponse;
import com.devteria.identityService.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc // tao mock request
// config override yaml
@TestPropertySource("/test.properties")
class UserControllerTest {
    @Autowired // mock request
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse response;
    private LocalDate dob;

    @BeforeEach
    public void initData() {
        dob = LocalDate.of(1990, 1, 22);
        request = UserCreationRequest.builder()
                .username("Join")
                .firstName("Join")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        response = UserResponse.builder()
                .id("8e872d231a1f")
                .username("Join")
                .firstName("Join")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN : data biet trc
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request); // serialize request -> to JSON
        // mock service
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);
        // WHEN,THEN : khi test cai gi
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("8e872d231a1f"));
        // THEN : khi xay ra thi lam gi
    }

    @Test
    void createUser_usernameInvalid_success() throws Exception {
        // GIVEN :
        request.setUsername("a");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // WHEN,THEN : khi test cai gi
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 3 characters"));
        // THEN : khi xay ra thi lam gi
    }
}
