package com.photoarchive.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Data
@Builder
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String surname;
    private String password;
    private boolean isEnabled;

    @OneToOne(mappedBy = "user")
    private Token token;

    public User(Long id, String username, String email, String firstName, String surname, String password, boolean isEnabled, Token token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.isEnabled = isEnabled;
        this.token = token;
    }

    public User(String username, String email, String firstName, String surname, String password, boolean isEnabled) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
