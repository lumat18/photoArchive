package com.photoarchive.managers;

import com.photoarchive.domain.User;
import com.photoarchive.exceptions.EmailNotFoundException;
import com.photoarchive.exceptions.TokenNotFoundException;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserManager implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenManager tokenManager;

    @Autowired
    public UserManager(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenManager tokenManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenManager = tokenManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    public User loadUserByEmail(String email) throws EmailNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);
    }

    public User loadUserByToken(String tokenValue) throws TokenNotFoundException {
        return tokenManager
                .findTokenByValue(tokenValue)
                .orElseThrow(TokenNotFoundException::new)
                .getUser();
    }

    public void saveUser(User user) throws UserAlreadyExistsException {
        if(existsByUsername(user.getUsername()) || existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException();
        }
        userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void enableUser(User user){
        user.setEnabled(true);
        updateUser(user);
    }

    public void setNewPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        updateUser(user);
    }

    private void updateUser(User user){
        userRepository.save(user);
    }
}
