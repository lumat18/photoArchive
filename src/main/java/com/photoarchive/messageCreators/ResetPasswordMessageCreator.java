package com.photoarchive.messageCreators;

import com.photoarchive.services.ResetCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component("reset")
public class ResetPasswordMessageCreator implements MessageCreator {

    private static final String SUBJECT = "PhotoARCHive reset password";
    private static final String TEXT = "In order to reset your password, please click" +
            "the link below and set a new password \n";

    private ResetCodeService resetCodeService;

    @Autowired
    public ResetPasswordMessageCreator(ResetCodeService resetCodeService) {
        this.resetCodeService = resetCodeService;
    }

    @Override
    public SimpleMailMessage createMessage(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(SUBJECT);
        message.setTo(email);
        message.setText(TEXT + createResetLink(email));
        return message;
    }

    private String createResetLink(String email) {
        return "http://localhost:8080/reset/process?value=" +
                resetCodeService.createResetCode(email);
    }
}
