package com.photoarchive.validators;

import com.photoarchive.annotations.MatchingPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchingPasswordValidator implements ConstraintValidator<MatchingPassword, MatchablePasswords> {
    @Override
    public boolean isValid(MatchablePasswords passwords, ConstraintValidatorContext context) {
        boolean isValid = passwords.getPassword().equals(passwords.getMatchingPassword());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("matchingPassword").addConstraintViolation();
        }
        return isValid;
    }

}
