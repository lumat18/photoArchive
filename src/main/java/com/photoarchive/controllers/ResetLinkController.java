package com.photoarchive.controllers;

import com.photoarchive.domain.User;
import com.photoarchive.managers.UserManager;
import com.photoarchive.messageCreation.MessageType;
import com.photoarchive.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/reset")
@Slf4j
public class ResetLinkController {

    private static final String LINK_SENT_MESSAGE = "Reset link was sent";
    private static final String LINK_NOT_SENT_MESSAGE = "No user with such an email found";

    private EmailService emailService;
    private UserManager userManager;

    @Autowired
    public ResetLinkController(EmailService emailService, UserManager userManager) {
        this.emailService = emailService;
        this.userManager = userManager;
    }

    @GetMapping
    public String showEmailInput() {
        return "email-input";
    }

    @PostMapping
    public String sendResetLink(@RequestParam String email, Model model) {
        Optional<User> user = userManager.loadUserByEmail(email);
        if (user.isPresent()){
            emailService.sendEmail(user.get(), MessageType.RESET);
            model.addAttribute("message", LINK_SENT_MESSAGE);
        }else {
            log.warn("User does not exist. Email was not sent");
            model.addAttribute("message", LINK_NOT_SENT_MESSAGE);
        }
        return "info-page";
    }
}
