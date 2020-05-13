package com.photoarchive.messageCreation;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import org.springframework.mail.SimpleMailMessage;

public interface MessageCreator {
    SimpleMailMessage create(String email, String tokenValue);
}
