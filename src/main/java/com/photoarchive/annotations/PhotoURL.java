package com.photoarchive.annotations;

import com.photoarchive.validators.PhotoURLValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhotoURLValidator.class)
public @interface PhotoURL {

    String message() default "Invalid url - not a photo";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
