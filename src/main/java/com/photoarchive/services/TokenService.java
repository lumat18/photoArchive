package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.repositories.TokenRepository;
import com.photoarchive.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token createToken(User user){
        Token token = new Token();
        String value = UUID.randomUUID().toString();
        token.setValue(value);
        token.setUser(user);
        return tokenRepository.save(token);
    }
    public Optional<Token> findTokenByValue(String value){
        return tokenRepository.findByValue(value);
    }
    public Optional<Token> findTokenByUsersEmail(String email){
        return tokenRepository.findByUser_Email(email);
    }
    public boolean existsByValue(String value){
        return tokenRepository.existsByValue(value);
    }
}
