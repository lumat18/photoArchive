package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.managers.UserManager;
import com.photoarchive.messageCreation.MessageType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PasswordResetMessageServiceTest {
    @MockBean
    private EmailService emailService;

    @Autowired
    private PasswordResetMessageService passwordResetMessageService;

    @Captor
    ArgumentCaptor<String> captor;

    @Captor
    ArgumentCaptor<MessageType> captorMessageType;

    @Captor
    ArgumentCaptor<SimpleMailMessage> captorMessage;

    @Test
    void shouldSendMessageToUser(){
        //given
        final User user = new User();
        user.setEmail("email@email.com");
        final Token token = new Token();
        user.setToken(token);
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        when(emailService.createMessage(user.getEmail(), token.getValue(), MessageType.RESET))
                .thenReturn(simpleMailMessage);
        //when
        passwordResetMessageService.sendPasswordResetMessageTo(user);
        verify(emailService).createMessage(captor.capture(), captor.capture(), captorMessageType.capture());
        verify(emailService).sendEmail(captorMessage.capture());
        //then
        assertThat(captor.getAllValues().get(0)).isEqualTo(user.getEmail());
        assertThat(captor.getAllValues().get(1)).isEqualTo(token.getValue());
        assertThat(captorMessageType.getValue()).isEqualTo(MessageType.RESET);
        assertThat(captorMessage.getValue()).isEqualTo(simpleMailMessage);
    }

}