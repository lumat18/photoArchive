package com.photoarchive.messageCreators;

import com.photoarchive.domain.Token;
import com.photoarchive.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Component("reset")
public class ResetPasswordMessageCreator implements MessageCreator {

    private static final String  SUBJECT = "PhotoARCHive reset password";
    private static final String TEXT = "In order to reset your password, please click" +
            "the link below and set a new password \n";

    private TokenService tokenService;

    @Autowired
    public ResetPasswordMessageCreator(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public SimpleMailMessage createMessage(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(SUBJECT);
        message.setTo(email);
        message.setText(TEXT+createResetLink(email));
        return message;
    }
    private String createResetLink(String email){
        return "http://localhost:8080/reset/process?value=" +
                getPasswordResetCode(email, getExpirationDate());
    }
    private String getExpirationDate(){
        return LocalDateTime.now().plusHours(24).toString();
    }

    private String getPasswordResetCode(String email, String expirationDate){
        Token token = tokenService.findTokenByUsersEmail(email).get();
        return Base64.getEncoder()
                .encodeToString((token.getValue()+"_"+expirationDate).getBytes());
    }
}
