package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.exceptions.TokenNotFoundException;
import com.photoarchive.managers.TokenManager;
import com.photoarchive.managers.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivationService {
    private TokenManager tokenManager;
    private UserManager userManager;

    @Autowired
    public ActivationService(TokenManager tokenManager, UserManager userManager) {
        this.tokenManager = tokenManager;
        this.userManager = userManager;
    }

    public void activate(String tokenValue) throws TokenNotFoundException {
        Token token = tokenManager.findTokenByValue(tokenValue)
                .orElseThrow(TokenNotFoundException::new);
        userManager.enableUser(token.getUser());
        log.info("User " + token.getUser().getUsername() + " was activated");
    }
}
