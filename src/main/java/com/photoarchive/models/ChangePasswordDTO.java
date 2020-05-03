package com.photoarchive.models;

import com.photoarchive.annotations.MatchingPassword;
import com.photoarchive.annotations.StrongPassword;
import com.photoarchive.validators.MatchablePasswords;
import lombok.Data;

@Data
@MatchingPassword
public class ChangePasswordDTO implements MatchablePasswords {
    @StrongPassword
    private String password;
    private String matchingPassword;
}
