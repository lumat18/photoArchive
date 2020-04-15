package com.photoarchive.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Start {
    private UserRepository userRepository;

    public Start(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;

        User user = User.builder()
                .username("tim123")
                .firstName("Tim")
                .surname("Buchalka")
                .email("tim@buchalka.com")
                .password(passwordEncoder.encode("123"))
                .isEnabled(true)
                .build();

        userRepository.save(user);

    }
}
