package com.photoarchive.validators;

import com.photoarchive.annotations.StrongPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.{5,15}$)");
    }
}
