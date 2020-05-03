package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.managers.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivationService {

    private UserManager userManager;

    @Autowired
    public ActivationService(UserManager userManager) {

        this.userManager = userManager;
    }

    public void activate(Token token) {
        userManager.enableUser(token.getUser());
        log.info("User " + token.getUser().getUsername() + " was activated");
    }
}
