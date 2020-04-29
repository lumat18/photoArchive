package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private TokenService tokenService;

    @Autowired
    public EmailService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void sendVerificationEmail(User user) {
        Token token = tokenService.createToken(user);
        String url = "http://localhost:8080/token?value=" + token.getValue();


    }


}
