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

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

@Controller
@RequestMapping("/reset-password")
@Slf4j
public class ChangePasswordController {

    private static final String POSITIVE_MESSAGE = "Reset link was sent";
    private static final String NEGATIVE_MESSAGE = "No user with such an email found";
    private static final String INVALID_LINK_MESSAGE = "Something went wrong. Your link is not valid.";
    private static final String EXPIRED_LINK_MESSAGE = "Link has expired";

    private EmailService emailService;
    private UserService userService;
    private TokenService tokenService;

    @Autowired
    public ChangePasswordController(EmailService emailService, UserService userService, TokenService tokenService) {
        this.emailService = emailService;
        this.userService = userService;
        this.tokenService = tokenService;
    }
    @GetMapping
    public String processPasswordReset(@RequestParam("value") String resetCode, Model model){
        String decodedLink = Arrays.toString(Base64.getDecoder().decode(resetCode));
        System.out.println("decodedLink = " + decodedLink);
        String tokenValue = StringUtils.substringBefore(decodedLink, "_");
        LocalDateTime expirationDate;
        try {
            expirationDate = LocalDateTime.parse(StringUtils.substringAfter(decodedLink, "_"));
        }catch (DateTimeException e){
            model.addAttribute("message", INVALID_LINK_MESSAGE);
            log.warn(e.getMessage());
            return "reset-password-error";
        }
        if (!tokenService.existsByValue(tokenValue)){
            model.addAttribute("message", INVALID_LINK_MESSAGE);
            return "reset-password-error";
        }
        if (expirationDate.isBefore(LocalDateTime.now())){
            model.addAttribute("message", EXPIRED_LINK_MESSAGE);
            return "reset-password-error";
        }
        return "redirect:/reset-password/change?value="+tokenValue;
    }

    @GetMapping("/change")
    public String changePassword(){
        return null;
    }

    @PostMapping
    public String sendResetLink(@RequestParam String email, Model model){
        if (userService.emailExists(email)){
            emailService.sendEmail(email, MessageType.RESET);
            model.addAttribute("messageSent", POSITIVE_MESSAGE);
        }else {
            model.addAttribute("messageSent", NEGATIVE_MESSAGE);
        }
        return "change-password";
    }
}
