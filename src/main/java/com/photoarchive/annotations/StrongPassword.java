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

    String message() default "Password length must be between 5-15 characters. It must contain at least one letter and one digit. ";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
