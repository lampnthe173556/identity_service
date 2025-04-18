package com.devteria.identityService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devteria.identityService.dto.request.RoleRequest;
import com.devteria.identityService.dto.response.RoleResponse;
import com.devteria.identityService.entities.Roles;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Roles toRole(RoleRequest request);

    RoleResponse toRoleResponse(Roles role);
}
