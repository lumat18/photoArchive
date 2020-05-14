package com.photoarchive.messageCreation.creators;

import com.photoarchive.messageCreation.MessageCreator;
import com.photoarchive.managers.ResetCodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component("reset")
public class ResetPasswordMessageCreator implements MessageCreator {

    private static final String SUBJECT = "PhotoARCHive reset password";
    private static final String TEXT = "In order to reset your password, please click" +
            "the link below and set a new password {0}\n";

    private ResetCodeManager resetCodeManager;

    @Autowired
    public ResetPasswordMessageCreator(ResetCodeManager resetCodeManager) {
        this.resetCodeManager = resetCodeManager;
    }

    @Override
    public SimpleMailMessage create(String email, String tokenValue) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(SUBJECT);
        message.setTo(email);
        message.setText(TEXT + createResetLink(tokenValue));
        return message;
    }

    private String createResetLink(String tokenValue){
        return "http://localhost:8080/change?value=" +
                resetCodeManager.createResetCode(tokenValue);
    }
}
