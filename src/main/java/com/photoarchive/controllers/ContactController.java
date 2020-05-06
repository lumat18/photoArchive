package com.photoarchive.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/contact")
@SessionAttributes("userInfo")
public class ContactController {
    @GetMapping
    public String showContactForm(){
        return "contact";
    }
}
