package com.photoarchive.controllers;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.exceptions.TokenNotFoundException;
import com.photoarchive.models.ChangePasswordDTO;
import com.photoarchive.services.TokenService;
import com.photoarchive.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Base64;

@Controller
@RequestMapping("/change")
@Slf4j
public class ChangePasswordController {

    private TokenService tokenService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @ModelAttribute(name = "changePasswordDTO")
    private ChangePasswordDTO changePasswordDTO(){
        return new ChangePasswordDTO();
    }

    @Autowired
    public ChangePasswordController(TokenService tokenService, UserService userService, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String changePassword(@ModelAttribute("value") String resetCode, Model model){
        model.addAttribute("resetCode", resetCode);
        return "new-password-input";
    }

    @PostMapping
    public String processPasswordChange(@Valid ChangePasswordDTO changePasswordDTO, Errors errors){
        byte[] decodedBytes = Base64.getDecoder().decode(changePasswordDTO.getResetCode());
        String decodedLink = new String(decodedBytes);
        String tokenValue = StringUtils.substringBefore(decodedLink, "_");
        Token token;
        try {
            token = tokenService.findTokenByValue(tokenValue).orElseThrow(TokenNotFoundException::new);
            User user = token.getUser();
            String pass = changePasswordDTO.getPassword();

            String encoded = passwordEncoder.encode(pass);

            user.setPassword(encoded);
            userService.saveUser(user);
        } catch (TokenNotFoundException e) {
            e.printStackTrace();
        }
        return "login";
    }


}
