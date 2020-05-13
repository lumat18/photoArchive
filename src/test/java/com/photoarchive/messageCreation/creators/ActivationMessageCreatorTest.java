package com.photoarchive.messageCreation.creators;

import com.photoarchive.managers.UserManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ActivationMessageCreatorTest {

    @MockBean
    private UserManager userManager;

    @Autowired
    private ActivationMessageCreator activationMessageCreator;

    @Test
    void shouldCreateActivationMessageForUser() {
        //given
        final String email = "testUserEmail";
        final String tokenValue = "testTokenValue";
        //when
        final SimpleMailMessage message = activationMessageCreator.create(email, tokenValue);
        //then
        assertThat(message).isNotNull();
        assertThat(Objects.requireNonNull(message.getTo())[0]).isEqualTo(email);
        assertThat(message.getText()).contains(tokenValue);
    }
}