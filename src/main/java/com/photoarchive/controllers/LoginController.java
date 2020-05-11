package com.photoarchive.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class LoginController {

    public static final String USER_NOT_ACTIVE_MESSAGE = "User account is not active";
    public static final String BAD_CREDENTIALS_MESSAGE = "Wrong username or password";


    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(HttpServletRequest request, Model model) {
        final AuthenticationException exception = (AuthenticationException) request
                .getSession()
                .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        if (exception instanceof DisabledException) {
            log.warn("Login error."+USER_NOT_ACTIVE_MESSAGE);
            model.addAttribute("message", USER_NOT_ACTIVE_MESSAGE);
        }
        if (exception instanceof BadCredentialsException) {
            log.warn("Login error."+BAD_CREDENTIALS_MESSAGE);
            model.addAttribute("message", BAD_CREDENTIALS_MESSAGE);
        }

        return "login";
    }
}

