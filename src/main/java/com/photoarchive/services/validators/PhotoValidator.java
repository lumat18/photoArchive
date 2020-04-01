package com.photoarchive.services.validators;

import com.photoarchive.annotations.Image;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class PhotoValidator implements ConstraintValidator<Image, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        BufferedImage read = null;
        try {
            read = ImageIO.read(multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return read != null;
    }

    @Override
    public void initialize(Image constraintAnnotation) {

    }
}
