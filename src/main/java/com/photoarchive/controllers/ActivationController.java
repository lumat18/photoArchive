package com.photoarchive.controllers;

import com.photoarchive.domain.Token;
import com.photoarchive.managers.TokenManager;
import com.photoarchive.services.ActivationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/activate")
@Slf4j
public class ActivationController {
    private static final String ACCOUNT_ACTIVATED_MESSAGE = "Account is activated!";
    private static final String ACCOUNT_NOT_ACTIVATED = "Failed to activate account";

    private ActivationService activationService;
    private TokenManager tokenManager;

    @Autowired
    public ActivationController(ActivationService activationService, TokenManager tokenManager) {
        this.activationService = activationService;
        this.tokenManager = tokenManager;
    }

    @GetMapping
    public String processAccountActivation(@RequestParam(name = "value") String tokenValue, Model model) {
        Optional<Token> token = tokenManager.findTokenByValue(tokenValue);
        if (token.isPresent()){
            activationService.activate(token.get());
            log.info("User account activated");
            model.addAttribute("message", ACCOUNT_ACTIVATED_MESSAGE);
            return "login";
       }
        model.addAttribute("message", ACCOUNT_NOT_ACTIVATED);
        log.warn("Request token was not found in database");
        return "info-page";
    }
}
