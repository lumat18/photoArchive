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
    private static final String LINK_NOT_SENT_MESSAGE = "Account does not exist or is not activated";

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

        String message = sendEmailIfUserExists(user);

        model.addAttribute("message", message);
        return "info-page";
    }

    private String sendEmailIfUserExists(Optional<User> user){
        if (user.isPresent()){
            return sendEmailIfUserIsEnabled(user.get());
        }else {
            log.warn("User does not exist. Email was not sent");
            return LINK_NOT_SENT_MESSAGE;
        }
    }

    private String sendEmailIfUserIsEnabled(User user){
        if (user.isEnabled()){
            emailService.sendEmail(user, MessageType.RESET);
            return LINK_SENT_MESSAGE;
        }else {
            log.warn("Request was made to sent reset link to user that is not enabled");
            return LINK_NOT_SENT_MESSAGE;
        }
    }

}
