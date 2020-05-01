package com.photoarchive.annotations;

import com.photoarchive.validators.PhotoFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhotoFileValidator.class)
public @interface PhotoFile {

    String message() default "Invalid photo format. ";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
