package com.devteria.identityService.controllers;

import com.devteria.identityService.dto.request.RoleRequest;
import com.devteria.identityService.dto.response.ApiResponse;
import com.devteria.identityService.dto.response.RoleResponse;
import com.devteria.identityService.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getListRole() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRole())
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<Void> deletePermission(@PathVariable("roleId") String roleId) {
        roleService.deleteRole(roleId);
        return ApiResponse.<Void>builder().build();
    }
}
