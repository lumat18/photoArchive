package com.photoarchive.controllers;

import com.photoarchive.exceptions.TokenNotFoundException;
import com.photoarchive.services.ActivationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/activate")
@Slf4j
public class ActivationController {
    private static final String ACCOUNT_ACTIVATED_MESSAGE = "Account is activated!";
    private static final String ACCOUNT_NOT_ACTIVATED = "Failed to activate account";

    private ActivationService activationService;

    @Autowired
    public ActivationController(ActivationService activationService) {
        this.activationService = activationService;
    }

    @GetMapping
    public String processAccountActivation(@RequestParam(name = "value") String token, Model model) {
        try {
            activationService.activate(token);
            log.info("User account activated");
        } catch (TokenNotFoundException e) {
            model.addAttribute("message", ACCOUNT_NOT_ACTIVATED);
            log.warn(e.getMessage());
            return "info-page";
        }
        model.addAttribute("message", ACCOUNT_ACTIVATED_MESSAGE);
        return "login";
    }
}
