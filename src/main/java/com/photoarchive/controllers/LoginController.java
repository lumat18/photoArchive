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

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(HttpServletRequest request, Model model) {
        final AuthenticationException exception = (AuthenticationException) request
                .getSession()
                .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        setProperAttributeMessage(exception, model);
        return "login";
    }

    private void setProperAttributeMessage(Exception exception, Model model){
        if (exception instanceof DisabledException) {
            log.warn("Login error. User not active");
            model.addAttribute("loginErrorMessage", "User account is not active");
        }
        if (exception instanceof BadCredentialsException) {
            log.warn("Login error. Bad credentials used");
            model.addAttribute("loginErrorMessage", "Wrong username or password");
        }
    }
}
