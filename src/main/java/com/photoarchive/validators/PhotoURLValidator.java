package com.photoarchive.validators;

import com.photoarchive.annotations.PhotoURL;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PhotoURLValidator implements ConstraintValidator<PhotoURL, String> {

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {
        BufferedImage read = null;
        try {
            read = ImageIO.read(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return read != null;
    }
}
