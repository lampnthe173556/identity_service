package com.devteria.identityService.dto.request;

import com.devteria.identityService.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;
    @DobConstraint(min = 18, message = "DOB_INVALID")
    LocalDate dob;
}
