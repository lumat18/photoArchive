package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.managers.TokenManager;
import com.photoarchive.managers.UserManager;
import com.photoarchive.messageCreation.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationService {

    private UserManager userManager;
    private TokenManager tokenManager;
    private EmailService emailService;

    @Autowired
    public RegistrationService(UserManager userManager, TokenManager tokenManager, EmailService emailService) {
        this.userManager = userManager;
        this.tokenManager = tokenManager;
        this.emailService = emailService;
    }

    public void register(User user) throws UserAlreadyExistsException {
        Token token = tokenManager.createToken();
        bindUserWithToken(user, token);
        sendActivationEmailTo(user.getEmail(), token.getValue());
        log.info("User " + user.getUsername() + " saved to database");
    }

    private void sendActivationEmailTo(String  email, String  tokenValue) {
        SimpleMailMessage activationMessage = emailService
                .createMessage(email, tokenValue, MessageType.ACTIVATION);
        emailService.sendEmail(activationMessage);
    }

    private void bindUserWithToken(User user, Token token) throws UserAlreadyExistsException {
        userManager.saveUser(user);
        token.setUser(user);
        tokenManager.saveToken(token);
    }
}
