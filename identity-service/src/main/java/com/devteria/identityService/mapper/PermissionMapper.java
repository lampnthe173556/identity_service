package com.devteria.identityService.mapper;

import org.mapstruct.Mapper;

import com.devteria.identityService.dto.request.PermissionRequest;
import com.devteria.identityService.dto.response.PermissionResponse;
import com.devteria.identityService.entities.Permissions;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permissions toPermissions(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permissions permission);
}
