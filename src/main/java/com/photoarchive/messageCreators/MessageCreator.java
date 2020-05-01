package com.photoarchive.messageCreators;

import org.springframework.mail.SimpleMailMessage;

public interface MessageCreator {
    SimpleMailMessage createMessage(String email);
}
