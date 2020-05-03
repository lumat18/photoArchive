package com.photoarchive.messageCreation.creators;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.messageCreation.MessageCreator;
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
    public SimpleMailMessage createMessage(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(SUBJECT);
        message.setTo(user.getEmail());
        message.setText(TEXT + createResetLink(user.getToken()));
        return message;
    }

    private String createResetLink(Token token){
        return "http://localhost:8080/change?value=" +
                resetCodeService.createResetCode(token);
    }
}
