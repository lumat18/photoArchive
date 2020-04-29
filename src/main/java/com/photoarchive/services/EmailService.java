package com.photoarchive.services;

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
        
    }


}
