package com.photoarchive.controllers;

import com.photoarchive.exceptions.EmailNotFoundException;
import com.photoarchive.messageCreation.MessageType;
import com.photoarchive.services.EmailService;
import com.photoarchive.services.ResetCodeService;
import com.photoarchive.managers.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DateTimeException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/reset")
@Slf4j
public class ResetLinkController {

    private static final String LINK_SENT_MESSAGE = "Reset link was sent";
    private static final String LINK_NOT_SENT_MESSAGE = "No user with such an email found";
    private static final String INVALID_LINK_MESSAGE = "Something went wrong. Your link is not valid. Try again";
    private static final String EXPIRED_LINK_MESSAGE = "Link has expired. Try Again";

    private EmailService emailService;
    private TokenManager tokenManager;
    private ResetCodeService resetCodeService;

    @Autowired
    public ResetLinkController(EmailService emailService, TokenManager tokenManager, ResetCodeService resetCodeService) {
        this.emailService = emailService;
        this.tokenManager = tokenManager;
        this.resetCodeService = resetCodeService;
    }

    @GetMapping
    public String showEmailInput() {
        return "email-input";
    }

    @PostMapping
    public String sendResetLink(@RequestParam String email, Model model) {
        try {
            emailService.sendEmail(email, MessageType.RESET);
            model.addAttribute("message", LINK_SENT_MESSAGE);
        } catch (EmailNotFoundException e) {
            log.warn(e.getMessage() + " Email was not sent");
            model.addAttribute("message", LINK_NOT_SENT_MESSAGE);
        }
        return "info-page";
    }

    @GetMapping("/process")
    public String processPasswordReset(@RequestParam("value") String resetCode, Model model, RedirectAttributes redirectAttributes) {

        String tokenValue = resetCodeService.extractTokenValue(resetCode);
        LocalDateTime expirationDate;
        try {
            expirationDate = resetCodeService.extractExpirationDate(resetCode);
        } catch (DateTimeException e) {
            model.addAttribute("message", INVALID_LINK_MESSAGE);
            log.warn(e.getMessage());
            return "email-input";
        }
        if (!tokenManager.existsByValue(tokenValue)) {
            model.addAttribute("message", INVALID_LINK_MESSAGE);
            return "email-input";
        }
        if (expirationDate.isBefore(LocalDateTime.now())) {
            model.addAttribute("message", EXPIRED_LINK_MESSAGE);
            return "email-input";
        }
        redirectAttributes.addFlashAttribute("value", resetCode);
        return "redirect:/change";
    }
}
