package com.photoarchive.messageCreation.creators;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.managers.TokenManager;
import com.photoarchive.managers.UserManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;

import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ActivationMessageCreatorTest {

    @MockBean
    private TokenManager tokenManager;
    @MockBean
    private UserManager userManager;

    @Autowired
    private ActivationMessageCreator activationMessageCreator;

    @Test
    void shouldCreateActivationMessageForUser() {
        //given
        final User user = new User();
        user.setEmail("testUserEmail");
        final Token token = new Token();
        final String tokenValue = "testTokenValue";
        token.setValue(tokenValue);
        when(tokenManager.createTokenFor(user)).thenReturn(token);
        //when
        final SimpleMailMessage message = activationMessageCreator.createMessage(user);
        //then
        assertThat(message).isNotNull();
        assertThat(Objects.requireNonNull(message.getTo())[0]).isEqualTo(user.getEmail());
        assertThat(message.getText()).contains(tokenValue);
        verify(tokenManager, times(1)).createTokenFor(user);
    }
}