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

    @Override
    public SimpleMailMessage create(String email , String tokenValue) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(SUBJECT);
        message.setTo(email);
        message.setText(TEXT + createActivationLink(tokenValue));
        return message;
    }

    private String createActivationLink(String tokenValue) {
        return "http://localhost:8080/activate?value=" + tokenValue;
    }
}
