package com.devteria.identityService.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.devteria.identityService.dto.request.PermissionRequest;
import com.devteria.identityService.dto.response.ApiResponse;
import com.devteria.identityService.dto.response.PermissionResponse;
import com.devteria.identityService.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getListPermission() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermission())
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiResponse<Void> deletePermission(@PathVariable("permissionId") String permissionId) {
        permissionService.deletePermission(permissionId);
        return ApiResponse.<Void>builder().build();
    }
}
