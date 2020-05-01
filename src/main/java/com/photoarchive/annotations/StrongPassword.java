package com.photoarchive.annotations;

import com.photoarchive.validators.StrongPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
public @interface StrongPassword {

    String message() default "Password length must be 5-8 characters & must contain a letter and a digit. ";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
