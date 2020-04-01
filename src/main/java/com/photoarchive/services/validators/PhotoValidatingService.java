package com.photoarchive.services.validators;

import com.photoarchive.exceptions.UnsupportedPhotoFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class PhotoValidatingService {

    public void isValid(MultipartFile multipartFile)throws UnsupportedPhotoFormatException{
        BufferedImage read = null;
        try {
            read = ImageIO.read(multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (read == null){
            throw new UnsupportedPhotoFormatException("Unsupported photo format");
        }
    }
}
