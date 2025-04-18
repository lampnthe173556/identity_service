package com.devteria.identityService.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devteria.identityService.dto.request.PermissionRequest;
import com.devteria.identityService.dto.response.PermissionResponse;
import com.devteria.identityService.entities.Permissions;
import com.devteria.identityService.mapper.PermissionMapper;
import com.devteria.identityService.repositories.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        Permissions permissions = permissionMapper.toPermissions(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permissions));
    }

    public List<PermissionResponse> getAllPermission() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }
}
