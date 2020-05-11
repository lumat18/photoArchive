package com.photoarchive.models;

import com.photoarchive.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String firstName;
    private String surname;
    private String email;

    public static UserInfo createUserInfo(User user){
        return new UserInfo(user.getFirstName(), user.getSurname(), user.getEmail());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
