package com.devteria.identityService.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

//tham so dau la annotation => muon validator, tham so thu 2 la kieu du lieu muon validation
public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;

    //moi khi constraint nay duoc khoi tao thi se lay duoc thong so
    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    //isValid xu ly ham dung hay khong
    //best practice: moi annotation se chiu trach nhiem cho 1 cai cu the
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)) {
            return true;
        }
        //chronoUnit tu tinh khoang cach ngay
        long year = ChronoUnit.YEARS.between(value, LocalDate.now());

        return year >= min;
    }
}
