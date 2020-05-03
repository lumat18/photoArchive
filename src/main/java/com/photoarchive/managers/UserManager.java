package com.photoarchive.managers;

import com.photoarchive.domain.User;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserManager implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    public Optional<User> loadUserByEmail(String email) {
        return userRepository
                .findByEmail(email);
    }

    public Optional<User> loadUserByToken(String tokenValue) {
        return userRepository
                .findByToken(tokenValue);
    }

    public void saveUser(User user) throws UserAlreadyExistsException {
        if (existsByUsername(user.getUsername()) || existsByEmail(user.getEmail())) {
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

    public void enableUser(User user) {
        user.setEnabled(true);
        updateUser(user);
    }

    public void setNewPassword(User user, String newPassword) {
        user.setPassword(newPassword);
        updateUser(user);
    }

    private void updateUser(User user) {
        userRepository.save(user);
    }
}
