package com.photoarchive.controllers;

import com.photoarchive.exceptions.EmailNotFoundException;
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

@Controller
@RequestMapping("/reset")
@Slf4j
public class ResetLinkController {

    private static final String LINK_SENT_MESSAGE = "Reset link was sent";
    private static final String LINK_NOT_SENT_MESSAGE = "No user with such an email found";

    private EmailService emailService;


    @Autowired
    public ResetLinkController(EmailService emailService) {
        this.emailService = emailService;
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

//    @GetMapping("/process")
//    public String processPasswordReset(@RequestParam("value") String resetCode, Model model, HttpSession session) {
//
//        String tokenValue = resetCodeService.extractTokenValue(resetCode);
//        LocalDateTime expirationDate;
//        try {
//            expirationDate = resetCodeService.extractExpirationDate(resetCode);
//        } catch (DateTimeException e) {
//            model.addAttribute("message", INVALID_LINK_MESSAGE);
//            log.warn(e.getMessage());
//            return "email-input";
//        }
//        if (!tokenManager.existsByValue(tokenValue)) {
//            model.addAttribute("message", INVALID_LINK_MESSAGE);
//            return "email-input";
//        }
//        if (expirationDate.isBefore(LocalDateTime.now())) {
//            model.addAttribute("message", EXPIRED_LINK_MESSAGE);
//            return "email-input";
//        }
//        session.setAttribute("resetCode", resetCode);
//        return "redirect:/change";
//    }
}
