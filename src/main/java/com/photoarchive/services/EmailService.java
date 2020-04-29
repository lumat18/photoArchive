package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.security.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final String SUBJECT = "PhotoARCHive verification email";

    private TokenService tokenService;
    private JavaMailSender javaMailSender;

    public EmailService(TokenService tokenService, JavaMailSender javaMailSender) {
        this.tokenService = tokenService;
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(User user) {
        Token token = tokenService.createToken(user);
        String url = "http://localhost:8080/token?value=" + token.getValue();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject();
    }


}
