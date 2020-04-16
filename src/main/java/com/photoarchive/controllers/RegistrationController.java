package com.photoarchive.controllers;

import com.photoarchive.models.RegistrationFormData;
import com.photoarchive.security.User;
import com.photoarchive.security.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@Slf4j
public class RegistrationController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @ModelAttribute(name = "registrationFormData")
    private RegistrationFormData registrationFormData(){
        return new RegistrationFormData();
    }

    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showRegistrationPage(){
        return "registration";
    }

    @PostMapping
    public String register(RegistrationFormData data){
        User user = userRepository.save(data.toUser(passwordEncoder));
        log.info("User "+user+" added to database");
        return "redirect:/login";
    }
}
