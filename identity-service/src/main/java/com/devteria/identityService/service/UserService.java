package com.devteria.identityService.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devteria.identityService.dto.request.UserCreationRequest;
import com.devteria.identityService.dto.request.UserUpdateRequest;
import com.devteria.identityService.dto.response.UserResponse;
import com.devteria.identityService.entities.User;
import com.devteria.identityService.enums.ErrorCode;
import com.devteria.identityService.enums.Roles;
import com.devteria.identityService.exception.AppException;
import com.devteria.identityService.mapper.UserMapper;
import com.devteria.identityService.repositories.RoleRepository;
import com.devteria.identityService.repositories.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor // khi khai bao contructor nay se tao ra cho tat ca cac bien ma defind co final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// bat ky field nao trong day khong khai bao gi mac dinh la private final
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {

        User user = userMapper.toUser(request);

        HashSet<com.devteria.identityService.entities.Roles> roles = new HashSet<>();
        roleRepository.findById(Roles.USER.name()).ifPresent(roles::add);
        user.setRoles(roles);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // @PreAuthorize(value = "hasRole('ADMIN')")//tim tat ca nhung scope co authority nao co prefix la Role moi duoc
    // accept
    // spring se tao proxy ngay trc ham nay,
    // truoc luc goi ham co role la admin thi moi hgoi ham
    @PreAuthorize(value = "hasAuthority('READ_DATA')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    // thuc hien xong roi moi kiem tra boi annotation
    public UserResponse getUser(String userId) {
        log.info("In method get detail user with postAuthorize");
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userRepository.deleteById(user.getId());
    }

    public UserResponse getUserByToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        return userMapper.toUserResponse(userRepository
                .findUserByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
