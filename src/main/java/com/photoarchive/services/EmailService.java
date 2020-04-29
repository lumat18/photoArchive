package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    private static final String SUBJECT = "PhotoARCHive verification email";

    private TokenService tokenService;
    private JavaMailSender javaMailSender;

    public EmailService(TokenService tokenService, JavaMailSender javaMailSender) {
        this.tokenService = tokenService;
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(User user) {
        SimpleMailMessage message = createMessageTo(user);
        javaMailSender.send(message);
        log.info("Verification email send to "+user.getEmail());
    }

    private SimpleMailMessage createMessageTo(User user){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(SUBJECT);
        message.setText("In order to validate your account, please click the link below: \n" + createActivationLink(user));
        return message;
    }
    
    private String createActivationLink(User user){
        Token token = tokenService.createToken(user);
        return "http://localhost:8080/token?value=" + token.getValue();
    }


}
