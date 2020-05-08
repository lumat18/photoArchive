package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.managers.UserManager;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ActivationServiceTest {
    @MockBean
    private UserManager userManager;

    @Captor
    ArgumentCaptor<User> captor;

    @Autowired
    private ActivationService activationService;

    @Test
    void shouldActivateUser(){
        final Token token = new Token();
        final User user = new User();
        token.setUser(user);

        activationService.activate(token);
        verify(userManager).enableUser(captor.capture());
        assertThat(user).isEqualTo(captor.getValue());
        verify(userManager).enableUser(user);
    }
}