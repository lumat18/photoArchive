package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.messageCreation.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetMessageService {

    private EmailService emailService;

    @Autowired
    public PasswordResetMessageService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendPasswordResetMessageTo(User user){
        Token token = user.getToken();
        SimpleMailMessage message = emailService
                .createMessage(user.getEmail(),token.getValue(), MessageType.RESET);
        emailService.sendEmail(message);
    }
}
