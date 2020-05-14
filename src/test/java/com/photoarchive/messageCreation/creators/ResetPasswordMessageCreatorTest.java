package com.photoarchive.messageCreation.creators;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.managers.UserManager;
import com.photoarchive.managers.ResetCodeManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class ResetPasswordMessageCreatorTest {

    @MockBean
    private ResetCodeManager resetCodeManager;
    @MockBean
    private UserManager userManager;

    @Autowired
    private ResetPasswordMessageCreator resetPasswordMessageCreator;

    @Test
    void shouldCreateResetPasswordMessageForUser(){
        //give
        final Token token = new Token();
        final String tokenValue = "testTokenValue";
        token.setValue(tokenValue);

        final User user = new User();
        user.setEmail("testUserEmail");
        user.setToken(token);
        
        when(resetCodeManager.createResetCode(tokenValue)).thenReturn(tokenValue);
        //when
        final SimpleMailMessage message = resetPasswordMessageCreator.create(user.getEmail(), token.getValue());
        //then
        assertThat(message).isNotNull();
        assertThat(Objects.requireNonNull(message.getTo())[0]).isEqualTo(user.getEmail());
        assertThat(message.getText()).contains(tokenValue);
        verify(resetCodeManager, times(1)).createResetCode(tokenValue);
        verifyNoMoreInteractions(resetCodeManager);
    }

}