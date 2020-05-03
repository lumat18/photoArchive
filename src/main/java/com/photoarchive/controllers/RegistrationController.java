package com.photoarchive.controllers;

import com.photoarchive.domain.User;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.models.UserDTO;
import com.photoarchive.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private static final String ACTIVATION_LINK_SENT_MESSAGE = "Activation link was sent, check your email";

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
        model.addAttribute("message", ACTIVATION_LINK_SENT_MESSAGE);
        return "info-page";
    }
}
