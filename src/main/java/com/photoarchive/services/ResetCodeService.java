package com.photoarchive.services;

import com.photoarchive.domain.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class ResetCodeService {

    private TokenService tokenService;

    @Autowired
    public ResetCodeService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public String createResetCode(String email) {
        Token token = tokenService.findTokenByUsersEmail(email).get();
        return Base64.getEncoder()
                .encodeToString((token.getValue() + "_" + getExpirationDate()).getBytes());
    }

    private String getExpirationDate() {
        return LocalDateTime.now().plusHours(24).toString();
    }
}
