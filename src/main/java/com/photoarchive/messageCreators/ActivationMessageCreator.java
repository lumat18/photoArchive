package com.photoarchive.messageCreators;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component("activation")
public class ActivationMessageCreator implements MessageCreator {

    private static final String SUBJECT = "PhotoARCHive activation email";

    private TokenService tokenService;

    @Autowired
    public ActivationMessageCreator(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public SimpleMailMessage createMessage(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(SUBJECT);
        message.setTo(user.getEmail());
        message.setText("In order to activate your account, please click the link below: \n" + createActivationLink(user));
        return message;
    }

    private String createActivationLink(User user) {
        Token token = tokenService.createToken(user);
        return "http://localhost:8080/register/token?value=" + token.getValue();
    }
}
