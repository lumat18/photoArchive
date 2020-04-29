package com.photoarchive.models;

import com.photoarchive.security.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationFormData {
    private String username;
    private String email;
    private String firstName;
    private String surname;
    private String password;

    public User toUser(PasswordEncoder encoder){
        return User.builder()
                .username(username)
                .email(email)
                .firstName(firstName)
                .surname(surname)
                .password(encoder.encode(password))
                .isEnabled(false)
                .build();
    }


}
