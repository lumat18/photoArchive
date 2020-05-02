package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.exceptions.TokenNotFoundException;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.messageCreators.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationService {

    private static final String USERNAME_ALREADY_EXISTS_MESSAGE = "User with this username already exists!";
    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "User with this email already exists!";

    private UserService userService;
    private EmailService emailService;
    private TokenService tokenService;

    @Autowired
    public RegistrationService(UserService userService, EmailService emailService, TokenService tokenService) {
        this.userService = userService;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    public void register(User user) throws UserAlreadyExistsException {
        checkUserName(user.getUsername());
        checkEmail(user.getEmail());
        userService.saveUser(user);
        emailService.sendEmail(user.getEmail(), MessageType.ACTIVATION);
        log.info("User " + user.getUsername() + " saved to database");
    }

    private void checkUserName(String username) throws UserAlreadyExistsException {
        if (userService.usernameExists(username)) {
            log.warn("User " + username + " already exists");
            throw new UserAlreadyExistsException(USERNAME_ALREADY_EXISTS_MESSAGE);
        }
    }

    private void checkEmail(String email) throws UserAlreadyExistsException {
        if (userService.emailExists(email)) {
            log.warn("User " + email + " already exists");
            throw new UserAlreadyExistsException(EMAIL_ALREADY_EXISTS_MESSAGE);
        }
    }

    public void activate(String tokenValue) throws TokenNotFoundException {
        Token token = tokenService.findTokenByValue(tokenValue)
                .orElseThrow(TokenNotFoundException::new);
        User user = token.getUser();
        user.setEnabled(true);
        userService.saveUser(user);
        log.info("User " + user.getUsername() + " was activated");
    }
}
