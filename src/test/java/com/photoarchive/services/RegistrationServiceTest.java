package com.photoarchive.services;

import com.photoarchive.domain.User;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.managers.UserManager;
import com.photoarchive.messageCreation.MessageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@SpringBootTest
class RegistrationServiceTest {
    @MockBean
    private UserManager userManager;
    @MockBean
    private EmailService emailService;
    @Autowired
    private RegistrationService registrationService;

    @Test
    void shouldThrowWhenTryingToRegisterAlreadyExistingUser() throws UserAlreadyExistsException {
        final User user = new User();
        doThrow(UserAlreadyExistsException.class).when(userManager).saveUser(user);

        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> registrationService.register(user));
        verify(userManager, times(1)).saveUser(user);
        verifyNoInteractions(emailService);
    }

    @Test
    void shouldRegisterNewUser() throws UserAlreadyExistsException {
        final User user = new User();
        registrationService.register(user);
        
        verify(userManager, times(1)).saveUser(user);
        verify(emailService, times(1)).sendEmail(user, MessageType.ACTIVATION);
    }

}