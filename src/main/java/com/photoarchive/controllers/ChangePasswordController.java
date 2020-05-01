package com.photoarchive.controllers;

import com.photoarchive.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/change-password")
@Slf4j
public class ChangePasswordController {

    private EmailService emailService;

    @Autowired
    public ChangePasswordController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public String sendResetLink(@RequestParam String email){
        return "/change-password";
    }
}
