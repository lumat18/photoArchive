package com.photoarchive.services.validators;

import com.photoarchive.exceptions.IncorrectUrlFormatException;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

@Service
public class UrlValidatingService {

        public void isValid(String url) throws IncorrectUrlFormatException {
            String[] schemes = {"http", "https"};
            UrlValidator urlValidator = new UrlValidator(schemes);

            if (!urlValidator.isValid(url)){
                throw new IncorrectUrlFormatException("Incorrect url format");
            }
        }
}
