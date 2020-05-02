package com.photoarchive.messageCreators;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.services.TokenService;
import com.photoarchive.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component("activation")
public class ActivationMessageCreator implements MessageCreator {

    private static final String SUBJECT = "PhotoARCHive activation email";
    private static final String TEXT = "In order to activate your account, please click the link below: \n";

    private TokenService tokenService;
    private UserService userService;

    @Autowired
    public ActivationMessageCreator(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public SimpleMailMessage createMessage(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(SUBJECT);
        message.setTo(email);
        message.setText(TEXT + createActivationLink(userService.loadUserByEmail(email)));
        return message;
    }

    private String createActivationLink(User user) {
        Token token = tokenService.createToken(user);
        return "http://localhost:8080/register/token?value=" + token.getValue();
    }
}
