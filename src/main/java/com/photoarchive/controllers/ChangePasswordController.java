package com.photoarchive.controllers;

import com.photoarchive.domain.User;
import com.photoarchive.managers.TokenManager;
import com.photoarchive.managers.UserManager;
import com.photoarchive.models.ChangePasswordDTO;
import com.photoarchive.services.ResetCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/change")
@Slf4j
@SessionAttributes("resetCode")
public class ChangePasswordController {

    private static final String PASSWORD_CHANGED_MESSAGE = "Password successfully changed";
    private static final String INVALID_LINK_MESSAGE = "Something went wrong. Your link is not valid. Try again";
    private static final String EXPIRED_LINK_MESSAGE = "Link has expired. Try Again";

    private UserManager userManager;
    private TokenManager tokenManager;
    private ResetCodeService resetCodeService;

    @ModelAttribute(name = "changePasswordDTO")
    private ChangePasswordDTO changePasswordDTO() {
        return new ChangePasswordDTO();
    }

    @ModelAttribute(name = "resetCode")
    public String resetCode() {
        return "";
    }

    @Autowired
    public ChangePasswordController(UserManager userManager, TokenManager tokenManager, ResetCodeService resetCodeService) {
        this.userManager = userManager;
        this.tokenManager = tokenManager;
        this.resetCodeService = resetCodeService;
    }

    @GetMapping
    public String processPasswordReset(@RequestParam("value") String resetCode, Model model) {

        String tokenValue = resetCodeService.extractTokenValue(resetCode);
        LocalDateTime tokenCreationDate;
        try {
            tokenCreationDate = resetCodeService.extractCreationDate(resetCode);
        } catch (DateTimeException e) {
            model.addAttribute("message", INVALID_LINK_MESSAGE);
            log.warn(e.getMessage());
            return "email-input";
        }
        if (!tokenManager.existsByValue(tokenValue)) {
            model.addAttribute("message", INVALID_LINK_MESSAGE);
            return "email-input";
        }
        if (tokenManager.hasExpired(tokenCreationDate)) {
            model.addAttribute("message", EXPIRED_LINK_MESSAGE);
            return "email-input";
        }
        model.addAttribute("resetCode", resetCode);
        return "new-password-input";
    }

    @PostMapping
    public String processPasswordChange(@Valid ChangePasswordDTO changePasswordDTO, Errors errors, @ModelAttribute(name = "resetCode") String resetCode, SessionStatus status, Model model) {
        String tokenValue = resetCodeService.extractTokenValue(resetCode);

        String newPassword = changePasswordDTO.getPassword();
        if (errors.hasErrors()) {
            return "new-password-input";
        }

        Optional<User> user = userManager.loadUserByToken(tokenValue);
        if (user.isPresent()) {
            userManager.setNewPassword(user.get(), newPassword);
            log.info("Password for user " + user.get().getUsername() + " was changed");
            model.addAttribute("message", PASSWORD_CHANGED_MESSAGE);
        }

        status.setComplete();
        return "login";
    }
}
