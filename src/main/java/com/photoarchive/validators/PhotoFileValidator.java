package com.photoarchive.validators;

import com.photoarchive.annotations.PhotoFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class PhotoFileValidator implements ConstraintValidator<PhotoFile, MultipartFile>{

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
}
