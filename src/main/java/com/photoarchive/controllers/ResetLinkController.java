package com.photoarchive.controllers;

import com.photoarchive.domain.User;
import com.photoarchive.managers.UserManager;
import com.photoarchive.services.PasswordResetMessageService;
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

    private UserManager userManager;
    private PasswordResetMessageService passwordResetMessageService;

    @Autowired
    public ResetLinkController(UserManager userManager, PasswordResetMessageService passwordResetMessageService) {
        this.userManager = userManager;
        this.passwordResetMessageService = passwordResetMessageService;
    }

    @GetMapping
    public String showEmailInput() {
        return "email-input";
    }

    @PostMapping
    public String processPasswordResetLinkSending(@RequestParam String email, Model model) {
        Optional<User> optionalUser = userManager.loadUserByEmail(email);

        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            if (user.isEnabled()){
                passwordResetMessageService.sendPasswordResetMessageTo(user);
                model.addAttribute("message", LINK_SENT_MESSAGE);
                return "info-page";
            }else {
                log.warn("Request was made by user that is not enabled");
            }
        }else {
            log.warn("User does not exist. Email was not sent");
        }
        model.addAttribute("message", LINK_NOT_SENT_MESSAGE);
        return "email-input";
    }
}
