package com.photoarchive.models;

import com.photoarchive.annotations.Email;
import com.photoarchive.annotations.MatchingPassword;
import com.photoarchive.annotations.StrongPassword;
import com.photoarchive.domain.User;
import com.photoarchive.validators.MatchablePasswords;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@MatchingPassword
public class UserDTO implements MatchablePasswords {
    private String username;
    @Email
    private String email;
    private String firstName;
    private String surname;
    @StrongPassword
    private String password;
    private String matchingPassword;

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
