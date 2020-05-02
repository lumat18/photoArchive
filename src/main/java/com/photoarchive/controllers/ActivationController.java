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
            model.addAttribute("invalidToken", e.getMessage());
            log.warn("Invalid token");
            return "registration";
        }
        return "redirect:/login";
    }
}
