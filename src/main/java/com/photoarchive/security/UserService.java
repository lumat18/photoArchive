package com.photoarchive.security;

import com.photoarchive.models.RegistrationFormData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (isNull(user)) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return user;
    }

    public boolean register(User user) {
        User existingUserWithSameUsername = userRepository.findByUsername(user.getUsername());

        if (isNull(existingUserWithSameUsername)){
            userRepository.save(user);
            log.info("User "+user.getUsername()+" saved to database");
            return true;
        }else {
            log.warn("User "+user.getUsername()+" already exists");
            return false;
        }
    }
}
