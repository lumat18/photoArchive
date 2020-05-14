package com.photoarchive.managers;

import com.photoarchive.domain.User;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserManagerTest {

    private final String EXISTING_EMAIL = "email";
    private final String NON_EXISTING_EMAIL = "noEmail";
    private final String EXISTING_USERNAME = "username";
    private final String NON_EXISTING_USERNAME = "noUsername";
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserManager userManager;

    @Captor
    private ArgumentCaptor<User> captor;

    @BeforeEach
    void setUp(){
        when(userRepository.findByUsername(EXISTING_USERNAME)).thenReturn(Optional.of(new User()));
        when(userRepository.findByUsername(NON_EXISTING_USERNAME)).thenReturn(Optional.empty());
        when(userRepository.existsByUsername(EXISTING_USERNAME)).thenReturn(true);
        when(userRepository.existsByEmail(EXISTING_EMAIL)).thenReturn(true);
        when(userRepository.existsByUsername(NON_EXISTING_USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(NON_EXISTING_EMAIL)).thenReturn(false);
    }

    @Test
    void shouldReturnUserIfUserNameExistsInDatabase(){
        //when
        UserDetails result = userManager.loadUserByUsername(EXISTING_USERNAME);
        //then
        assertThat(result).isNotNull();
        verify(userRepository,times(1)).findByUsername(EXISTING_USERNAME);
    }
    @Test
    void shouldThrowIfUsernameDoesNotExistInDatabase(){

        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(()->userManager.loadUserByUsername(NON_EXISTING_USERNAME));
    }

    @Test
    void shouldNotThrowIfSavedUserHasNewUsernameAndNewEmail(){
        //given
        User userToSave = new User();
        userToSave.setUsername(NON_EXISTING_USERNAME);
        userToSave.setEmail(NON_EXISTING_EMAIL);
        //when
        //then
        assertDoesNotThrow(() -> userManager.saveUser(userToSave));
    }
    @Test
    void shouldThrowIfSavedUserHasExistingUsernameAndExistingEmail(){
        //given
        User userToSave = new User();
        userToSave.setUsername(EXISTING_USERNAME);
        userToSave.setEmail(EXISTING_EMAIL);
        //when
        //then
        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> userManager.saveUser(userToSave));
    }
    @Test
    void shouldThrowIfSavedUserHasNewUsernameAndExistingEmail(){
        //given
        User userToSave = new User();
        userToSave.setUsername(NON_EXISTING_USERNAME);
        userToSave.setEmail(EXISTING_EMAIL);
        //when
        //then
        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> userManager.saveUser(userToSave));
    }
    @Test
    void shouldThrowIfSavedUserHasExistingUsernameAndNewEmail(){
        //given
        User userToSave = new User();
        userToSave.setUsername(EXISTING_USERNAME);
        userToSave.setEmail(NON_EXISTING_EMAIL);
        //when
        //then
        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> userManager.saveUser(userToSave));
    }

    @Test
    void shouldEnableUser(){
        //given
        final User userToBeEnabled = new User();
        userToBeEnabled.setEnabled(false);
        when(userRepository.save(userToBeEnabled)).thenReturn(userToBeEnabled);
        //when
        userManager.enableUser(userToBeEnabled);
        verify(userRepository).save(captor.capture());
        //then
        assertThat(captor.getValue().isEnabled()).isTrue();
        verify(userRepository, times(1)).save(userToBeEnabled);
    }

    @Test
    void shouldSetNewPasswordForUser(){
        //given
        final User user = new User();
        final String newPassword = "newPassword";
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        //when
        userManager.setNewPassword(user, newPassword);
        verify(userRepository).save(captor.capture());
        //then
        verify(userRepository, times(1)).save(user);
        assertThat(captor.getValue().getPassword()).isEqualTo(newPassword);
    }
}