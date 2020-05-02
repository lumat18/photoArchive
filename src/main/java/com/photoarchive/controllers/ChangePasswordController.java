package com.photoarchive.controllers;

import com.photoarchive.domain.User;
import com.photoarchive.exceptions.TokenNotFoundException;
import com.photoarchive.models.ChangePasswordDTO;
import com.photoarchive.services.ResetCodeService;
import com.photoarchive.managers.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/change")
@Slf4j
public class ChangePasswordController {

    private UserManager userManager;
    private ResetCodeService resetCodeService;
    private PasswordEncoder passwordEncoder;

    @ModelAttribute(name = "changePasswordDTO")
    private ChangePasswordDTO changePasswordDTO() {
        return new ChangePasswordDTO();
    }

    @Autowired
    public ChangePasswordController(UserManager userManager, ResetCodeService resetCodeService, PasswordEncoder passwordEncoder) {
        this.userManager = userManager;
        this.resetCodeService = resetCodeService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String changePassword(@ModelAttribute("value") String resetCode, Model model) {
        model.addAttribute("resetCode", resetCode);
        return "new-password-input";
    }

    @PostMapping
    public String processPasswordChange(@Valid ChangePasswordDTO changePasswordDTO, Errors errors) {
        String tokenValue = resetCodeService.extractTokenValue(changePasswordDTO.getResetCode());
        String newPassword = changePasswordDTO.getPassword();
        try {
            User user = userManager.loadUserByToken(tokenValue);
            userManager.setNewPassword(user, passwordEncoder.encode(newPassword));
            log.info("Password for user " + user.getUsername() + " was changed");
        } catch (TokenNotFoundException e) {
            log.warn(e.getMessage());
        }
        return "login";
    }
}
