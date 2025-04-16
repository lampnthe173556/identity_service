package com.devteria.identityService.controllers;

import com.devteria.identityService.dto.response.ApiResponse;
import com.devteria.identityService.dto.request.UserCreationRequest;
import com.devteria.identityService.dto.request.UserUpdateRequest;
import com.devteria.identityService.dto.response.UserResponse;
import com.devteria.identityService.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    //@Valid thi se giup validation nhung cai role define trong object
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        //requestBody : map data tu body vao object duoi code

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));

        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        //get thong tin dang duoc dang nhap trong request cua spring security
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        //mac dinh jwt authentication manager se tu dong map scope vao scope cua minh nen no se no scope vao duoi
//        log.info("Roles: {}", authentication.getAuthorities().stream().toList());
        authentication.getAuthorities().forEach(grantedAuthority -> {
            log.info(grantedAuthority.getAuthority());
        });
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @PostMapping("/userDetail")
    public ApiResponse<UserResponse> getUserDetail() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserByToken())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@RequestBody UserUpdateRequest request, @PathVariable("userId") String userId) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
