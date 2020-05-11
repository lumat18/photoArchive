package com.photoarchive.managers;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenManager {

    public static final Integer TOKEN_EXPIRATION_TIME_IN_HOURS=24;

    private TokenRepository tokenRepository;

    @Autowired
    public TokenManager(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token createTokenFor(User user) {
        Token token = new Token();
        String value = UUID.randomUUID().toString();
        token.setValue(value);
        token.setUser(user);
        return tokenRepository.save(token);
    }

    public Optional<Token> findTokenByValue(String value) {
        return tokenRepository.findByValue(value);
    }

    public boolean existsByValue(String value) {
        return tokenRepository.existsByValue(value);
    }

    public boolean hasExpired(LocalDateTime tokenCreationDate) {
        return tokenCreationDate.isBefore(LocalDateTime.now().minusHours(TOKEN_EXPIRATION_TIME_IN_HOURS));
    }
}
