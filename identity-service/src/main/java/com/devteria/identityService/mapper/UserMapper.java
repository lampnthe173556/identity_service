package com.devteria.identityService.mapper;

import com.devteria.identityService.dto.request.UserCreationRequest;
import com.devteria.identityService.dto.request.UserUpdateRequest;
import com.devteria.identityService.dto.response.UserResponse;
import com.devteria.identityService.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    //@MappingTarget giup mapping data tu request vao user
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toListUserResponse(List<User> userList);
}
