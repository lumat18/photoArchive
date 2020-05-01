package com.photoarchive.annotations;

import com.photoarchive.validators.PhotoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhotoValidator.class)
public @interface PhotoFile {

    String message() default "Invalid Photo Format. ";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
