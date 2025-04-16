package com.devteria.identityService.dto.request;

import com.devteria.identityService.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;
    @DobConstraint(min = 18, message = "DOB_INVALID")
    LocalDate dob;
    List<String> roles;
}
