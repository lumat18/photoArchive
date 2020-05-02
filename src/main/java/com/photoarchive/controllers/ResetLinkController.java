package com.photoarchive.controllers;

import com.photoarchive.messageCreators.MessageType;
import com.photoarchive.services.EmailService;
import com.photoarchive.services.TokenService;
import com.photoarchive.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Arrays;
import java.util.Base64;

@Controller
@RequestMapping("/reset")
@Slf4j
public class ResetLinkController {

    private static final String POSITIVE_MESSAGE = "Reset link was sent";
    private static final String NEGATIVE_MESSAGE = "No user with such an email found";
    private static final String INVALID_LINK_MESSAGE = "Something went wrong. Your link is not valid.";
    private static final String EXPIRED_LINK_MESSAGE = "Link has expired";

    private EmailService emailService;
    private UserService userService;
    private TokenService tokenService;

    @Autowired
    public ResetLinkController(EmailService emailService, UserService userService, TokenService tokenService) {
        this.emailService = emailService;
        this.userService = userService;
        this.tokenService = tokenService;
    }
    @GetMapping
    public String showEmailInput(){
        return "email-input";
    }

    @GetMapping("/process")
    public String processPasswordReset(@RequestParam("value") String resetCode, Model model, RedirectAttributes redirectAttributes){
        byte[] decodedBytes = Base64.getDecoder().decode(resetCode);
        String decodedLink = new String(decodedBytes);
        String tokenValue = StringUtils.substringBefore(decodedLink, "_");
        LocalDateTime expirationDate;
        try {
            expirationDate = LocalDateTime.parse(StringUtils.substringAfter(decodedLink, "_"));
        }catch (DateTimeException e){
            model.addAttribute("message", INVALID_LINK_MESSAGE);
            log.warn(e.getMessage());
            return "email-input";
        }
        if (!tokenService.existsByValue(tokenValue)){
            model.addAttribute("message", INVALID_LINK_MESSAGE);
            return "email-input";
        }
        if (expirationDate.isBefore(LocalDateTime.now())){
            model.addAttribute("message", EXPIRED_LINK_MESSAGE);
            return "email-input";
        }
        redirectAttributes.addFlashAttribute("value", resetCode);
        return "redirect:/change";
    }

    @PostMapping
    public String sendResetLink(@RequestParam String email, Model model){
        if (userService.emailExists(email)){
            emailService.sendEmail(email, MessageType.RESET);
            model.addAttribute("message", POSITIVE_MESSAGE);
        }else {
            model.addAttribute("message", NEGATIVE_MESSAGE);
        }
        return "email-input";
    }
}
