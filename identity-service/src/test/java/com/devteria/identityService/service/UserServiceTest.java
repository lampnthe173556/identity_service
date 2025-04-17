package com.devteria.identityService.service;

import com.devteria.identityService.dto.request.UserCreationRequest;
import com.devteria.identityService.dto.response.UserResponse;
import com.devteria.identityService.entities.User;
import com.devteria.identityService.exception.AppException;
import com.devteria.identityService.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@TestPropertySource(value = "/test.properties")
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse response;
    private User user;
    private LocalDate dob;

    @BeforeEach
    public void initData() {
        dob = LocalDate.of(1990, 1, 22);
        request = UserCreationRequest
                .builder()
                .username("Join")
                .firstName("Join")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        response = UserResponse
                .builder()
                .id("8e872d231a1f")
                .username("Join")
                .firstName("Join")
                .lastName("Doe")
                .dob(dob)
                .build();
        user = User.builder()
                .id("8e872d231a1f")
                .username("Join")
                .firstName("Join")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        //GIVEN
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);

        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
        //WHEN, THEN
        var response = userService.createUser(request);
        //THEN
        Assertions.assertThat(response.getId()).isEqualTo("8e872d231a1f");
        Assertions.assertThat(response.getUsername()).isEqualTo("Join");
    }

    @Test
    void createUser_userExisted_success() {
        //GIVEN
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(true);

        //WHEN, THEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        //THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    @Test
    @WithMockUser(username = "join")
    void getMyInfo_valid_success() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        var response = userService.getUserByToken();
        Assertions.assertThat(response.getUsername()).isEqualTo("Join");
        Assertions.assertThat(response.getId()).isEqualTo("8e872d231a1f");
    }

    @Test
    @WithMockUser(username = "join")
    void getMyInfo_notExisted() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        var exception = assertThrows(AppException.class, () -> userService.getUserByToken());
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);

    }
}