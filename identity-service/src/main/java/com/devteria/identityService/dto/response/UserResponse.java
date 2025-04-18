package com.devteria.identityService.dto.response;

import java.time.LocalDate;
import java.util.Set;

import com.devteria.identityService.entities.Roles;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate dob;
    Set<Roles> roles;
}
