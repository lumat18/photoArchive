package com.photoarchive.annotations;

import com.photoarchive.validators.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EmailValidator.class)
public @interface Email {

    String message() default "Invalid email format. ";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
