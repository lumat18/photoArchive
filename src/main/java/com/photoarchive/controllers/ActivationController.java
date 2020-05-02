package com.photoarchive.controllers;

import com.photoarchive.exceptions.TokenNotFoundException;
import com.photoarchive.services.ActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/activate")
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
        } catch (TokenNotFoundException e) {
            model.addAttribute("invalidToken", e.getMessage());
            return "registration";
        }
        return "redirect:/login";
    }
}
