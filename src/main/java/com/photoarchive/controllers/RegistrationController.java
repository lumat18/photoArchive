package com.photoarchive.controllers;

import com.photoarchive.domain.User;
import com.photoarchive.exceptions.TokenNotFoundException;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.models.UserDTO;
import com.photoarchive.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private RegistrationService registrationService;
    private PasswordEncoder passwordEncoder;

    @ModelAttribute(name = "userDTO")
    private UserDTO userDTO() {
        return new UserDTO();
    }

    @Autowired
    public RegistrationController(RegistrationService registrationService, PasswordEncoder passwordEncoder) {
        this.registrationService = registrationService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showRegistrationPage() {
        return "registration";
    }

    @GetMapping("/token")
    public String processAccountActivation(@RequestParam(name = "value") String token, Model model) {
        try {
            registrationService.activate(token);
        } catch (TokenNotFoundException e) {
            model.addAttribute("invalidToken", e.getMessage());
            return "registration";
        }
        return "redirect:/login";
    }

    @PostMapping
    public String register(@Valid UserDTO userDTO, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "registration";
        }
        User user = userDTO.toUser(passwordEncoder);
        try {
            registrationService.register(user);
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("userAlreadyExists", e.getMessage());
            return "registration";
        }
        return "redirect:/login";
    }
}
