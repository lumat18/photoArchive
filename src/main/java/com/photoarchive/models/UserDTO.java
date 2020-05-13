package com.photoarchive.models;

import com.photoarchive.annotations.Email;
import com.photoarchive.annotations.MatchingPassword;
import com.photoarchive.annotations.StrongPassword;
import com.photoarchive.validators.MatchablePasswords;
import lombok.Data;

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
}
