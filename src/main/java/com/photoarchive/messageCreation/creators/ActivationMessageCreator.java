package com.photoarchive.messageCreation.creators;

import com.photoarchive.domain.Token;
import com.photoarchive.exceptions.EmailNotFoundException;
import com.photoarchive.messageCreation.MessageCreator;
import com.photoarchive.managers.TokenManager;
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
    public SimpleMailMessage createMessage(String email) throws EmailNotFoundException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(SUBJECT);
        message.setTo(email);
        message.setText(TEXT + createActivationLink(email));
        return message;
    }

    private String createActivationLink(String email) throws EmailNotFoundException {
        Token token = tokenManager.createToken(email);
        return "http://localhost:8080/activate?value=" + token.getValue();
    }
}
