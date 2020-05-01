package com.photoarchive.annotations;

import com.photoarchive.validators.MatchingPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MatchingPasswordValidator.class)
public @interface MatchingPassword {

    String message() default "Password does not match ";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
