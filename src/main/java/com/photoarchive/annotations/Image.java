package com.photoarchive.annotations;

import com.photoarchive.services.validators.PhotoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhotoValidator.class)
public @interface Image {

    String message() default "Invalid Photo Format";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
