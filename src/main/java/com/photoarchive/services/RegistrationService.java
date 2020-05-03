package com.photoarchive.services;

import com.photoarchive.domain.User;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.managers.UserManager;
import com.photoarchive.messageCreation.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationService {

    private UserManager userManager;
    private EmailService emailService;

    @Autowired
    public RegistrationService(UserManager userManager, EmailService emailService) {
        this.userManager = userManager;
        this.emailService = emailService;
    }

    public void register(User user) throws UserAlreadyExistsException {
        userManager.saveUser(user);
        emailService.sendEmail(user, MessageType.ACTIVATION);
        log.info("User " + user.getUsername() + " saved to database");
    }
}
