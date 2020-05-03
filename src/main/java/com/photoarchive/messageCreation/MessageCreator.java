package com.photoarchive.messageCreation;

import com.photoarchive.domain.User;
import org.springframework.mail.SimpleMailMessage;

public interface MessageCreator {
    SimpleMailMessage createMessage(User user);
}
