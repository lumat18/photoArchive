package com.photoarchive.controllers;

import com.photoarchive.models.RegistrationFormData;
import com.photoarchive.security.User;
import com.photoarchive.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private static final String USER_ALREADY_EXISTS_MESSAGE = "User with this username already exists!";

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @ModelAttribute(name = "registrationFormData")
    private RegistrationFormData registrationFormData(){
        return new RegistrationFormData();
    }

    @Autowired
    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showRegistrationPage(){
        return "registration";
    }

    @PostMapping
    public String register(RegistrationFormData data, Model model){
        User user = data.toUser(passwordEncoder);
        boolean registered = userService.register(user);
        if (!registered){
            model.addAttribute("userAlreadyExists", USER_ALREADY_EXISTS_MESSAGE);
            return "registration";
        }
        return "redirect:/login";
    }
}
