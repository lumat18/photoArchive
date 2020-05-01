package com.photoarchive.controllers;

import com.photoarchive.exceptions.TokenNotFoundException;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.models.UserDTO;
import com.photoarchive.domain.User;
import com.photoarchive.services.UserService;
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

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @ModelAttribute(name = "userDTO")
    private UserDTO userDTO(){
        return new UserDTO();
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

    @GetMapping("/token")
    public String processAccountActivation(@RequestParam String value, Model model){
        try {
            userService.activate(value);
        } catch (TokenNotFoundException e) {
            model.addAttribute("invalidToken", e.getMessage());
            return "registration";
        }
        return "redirect:/login";
    }

    @PostMapping
    public String register(@Valid UserDTO userDTO, Errors errors, Model model){
        if (errors.hasErrors()){
            return "registration";
        }
        User user = userDTO.toUser(passwordEncoder);
        try {
            userService.register(user);
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("userAlreadyExists", e.getMessage());
            return "registration";
        }
        return "redirect:/login";
    }
}
