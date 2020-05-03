package com.photoarchive.messageCreation.creators;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.managers.TokenManager;
import com.photoarchive.messageCreation.MessageCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component("activation")
public class ActivationMessageCreator implements MessageCreator {

    private static final String SUBJECT = "PhotoARCHive activation email";
    private static final String TEXT = "In order to activate your account, please click the link below: \n";

    private TokenManager tokenManager;

    @Autowired
    public ActivationMessageCreator(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public SimpleMailMessage createMessage(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(SUBJECT);
        message.setTo(user.getEmail());
        message.setText(TEXT + createActivationLink(user));
        return message;
    }

    private String createActivationLink(User user) {
        Token token = tokenManager.createTokenFor(user);
        return "http://localhost:8080/activate?value=" + token.getValue();
    }
}
