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

    public UserInfo(User user) {
        this.firstName = user.getFirstName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
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
