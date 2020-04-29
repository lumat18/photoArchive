package com.photoarchive.security;

import com.photoarchive.domain.Token;
import com.photoarchive.exceptions.TokenNotFoundException;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.services.EmailService;
import com.photoarchive.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private static final String USERNAME_ALREADY_EXISTS_MESSAGE = "User with this username already exists!";
    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "User with this email already exists!";


    private UserRepository userRepository;
    private EmailService emailService;
    private TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (isNull(user)) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return user;
    }

    public void register(User user) throws UserAlreadyExistsException {
        if (usernameExists(user.getUsername())){
            log.warn("User "+user.getUsername()+" already exists");
            throw new UserAlreadyExistsException(USERNAME_ALREADY_EXISTS_MESSAGE);
        }
        if (emailExists(user.getEmail())){
            log.warn("User "+user.getEmail()+" already exists");
            throw new UserAlreadyExistsException(EMAIL_ALREADY_EXISTS_MESSAGE);
        }
        userRepository.save(user);
        emailService.sendVerificationEmail(user);
        log.info("User "+user.getUsername()+" saved to database");
    }

    private boolean usernameExists(String username){
        return userRepository.findByUsername(username) != null;
    }
    private boolean emailExists(String email){
        return userRepository.findByEmail(email) != null;
    }

    public void activate(String tokenValue) throws TokenNotFoundException {
        Token token = tokenService.findTokenByValue(tokenValue)
                .orElseThrow(TokenNotFoundException::new);
        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }
}
