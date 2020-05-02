package com.photoarchive.messageCreation;

import com.photoarchive.exceptions.EmailNotFoundException;
import org.springframework.mail.SimpleMailMessage;

public interface MessageCreator {
    SimpleMailMessage createMessage(String email) throws EmailNotFoundException;
}
