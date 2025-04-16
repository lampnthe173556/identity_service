package com.devteria.identityService.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {DobValidator.class}
)//class chiu trach nhiem validate

public @interface DobConstraint {
    String message() default "Invalid dob";

    int min();//khong de default la bat buoc nhap

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
