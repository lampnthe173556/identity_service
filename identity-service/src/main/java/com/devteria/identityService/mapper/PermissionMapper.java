package com.devteria.identityService.mapper;


import com.devteria.identityService.dto.request.PermissionRequest;
import com.devteria.identityService.dto.response.PermissionResponse;
import com.devteria.identityService.entities.Permissions;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permissions toPermissions(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permissions permission);
}
