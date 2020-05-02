package com.photoarchive.managers;

import com.photoarchive.domain.Token;
import com.photoarchive.exceptions.EmailNotFoundException;
import com.photoarchive.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenManager {

    private TokenRepository tokenRepository;
    private UserManager userManager;

    @Autowired
    public TokenManager(TokenRepository tokenRepository, UserManager userManager) {
        this.tokenRepository = tokenRepository;
        this.userManager = userManager;
    }

    public Token createToken(String email) throws EmailNotFoundException {
        Token token = new Token();
        String value = UUID.randomUUID().toString();
        token.setValue(value);
        token.setUser(userManager.loadUserByEmail(email));
        return tokenRepository.save(token);
    }

    public Optional<Token> findTokenByValue(String value) {
        return tokenRepository.findByValue(value);
    }

    public Optional<Token> findTokenByUsersEmail(String email) {
        return tokenRepository.findByUser_Email(email);
    }

    public boolean existsByValue(String value) {
        return tokenRepository.existsByValue(value);
    }
}
