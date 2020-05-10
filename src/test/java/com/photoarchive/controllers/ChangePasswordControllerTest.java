package com.photoarchive.controllers;

import com.photoarchive.domain.User;
import com.photoarchive.managers.TokenManager;
import com.photoarchive.managers.UserManager;
import com.photoarchive.models.ChangePasswordDTO;
import com.photoarchive.services.ResetCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChangePasswordController.class)
class ChangePasswordControllerTest {
    private static final String PASSWORD_CHANGED_MESSAGE = "Password successfully changed";
    private static final String INVALID_LINK_MESSAGE = "Something went wrong. Your link is not valid. Try again";
    private static final String EXPIRED_LINK_MESSAGE = "Link has expired. Try Again";

    @MockBean
    private UserManager userManager;
    @MockBean
    private TokenManager tokenManager;
    @MockBean
    private ResetCodeService resetCodeService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private String resetCode;
    private String tokenValue;
    private LocalDateTime creationDate;
    private User user;
    private String newPassword;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        resetCode = "resetCode";
        tokenValue = "tokenValue";
        creationDate = LocalDateTime.now();
        user = new User();
        newPassword = "newPassword1";
    }

    @Test
    void shouldNotProcessResetWhenResetCodeIsWrong() throws Exception {
        when(resetCodeService.extractTokenValue(resetCode)).thenReturn(null);
        when(resetCodeService.extractTokenCreationDate(resetCode)).thenThrow(DateTimeException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/change")
                .param("value", resetCode))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", INVALID_LINK_MESSAGE))
                .andExpect(view().name("email-input"));

        verify(resetCodeService, times(1)).extractTokenValue(resetCode);
        verify(resetCodeService, times(1)).extractTokenCreationDate(resetCode);
        verifyNoInteractions(userManager, tokenManager, passwordEncoder);
    }

    @Test
    void shouldNotProcessWhenTokenDoesntExist() throws Exception {
        when(resetCodeService.extractTokenValue(resetCode)).thenReturn(tokenValue);
        when(resetCodeService.extractTokenCreationDate(resetCode)).thenReturn(creationDate);
        when(tokenManager.existsByValue(tokenValue)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/change")
                .param("value", resetCode))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", INVALID_LINK_MESSAGE))
                .andExpect(view().name("email-input"));

        verify(resetCodeService, times(1)).extractTokenValue(resetCode);
        verify(resetCodeService, times(1)).extractTokenCreationDate(resetCode);
        verify(tokenManager, times(1)).existsByValue(tokenValue);
        verifyNoInteractions(userManager, passwordEncoder);
    }

    @Test
    void shouldNotProcessWhenTokenHasExpired() throws Exception {
        when(resetCodeService.extractTokenValue(resetCode)).thenReturn(tokenValue);
        when(resetCodeService.extractTokenCreationDate(resetCode)).thenReturn(creationDate);
        when(tokenManager.existsByValue(tokenValue)).thenReturn(true);
        when(tokenManager.hasExpired(creationDate)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/change")
                .param("value", resetCode))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", EXPIRED_LINK_MESSAGE))
                .andExpect(view().name("email-input"));
        verify(resetCodeService, times(1)).extractTokenValue(resetCode);
        verify(resetCodeService, times(1)).extractTokenCreationDate(resetCode);
        verify(tokenManager, times(1)).existsByValue(tokenValue);
        verify(tokenManager, times(1)).hasExpired(creationDate);
        verifyNoInteractions(userManager, passwordEncoder);
    }

    @Test
    void shouldProcessPasswordReset() throws Exception {
        when(resetCodeService.extractTokenValue(resetCode)).thenReturn(tokenValue);
        when(resetCodeService.extractTokenCreationDate(resetCode)).thenReturn(creationDate);
        when(tokenManager.existsByValue(tokenValue)).thenReturn(true);
        when(tokenManager.hasExpired(creationDate)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/change")
                .param("value", resetCode))
                .andExpect(status().isOk())
                .andExpect(model().attribute("resetCode", resetCode))
                .andExpect(view().name("new-password-input"));
        verify(resetCodeService, times(1)).extractTokenValue(resetCode);
        verify(resetCodeService, times(1)).extractTokenCreationDate(resetCode);
        verify(tokenManager, times(1)).existsByValue(tokenValue);
        verify(tokenManager, times(1)).hasExpired(creationDate);
        verifyNoInteractions(userManager, passwordEncoder);
    }

    @Test
    void shouldProcessPasswordChange() throws Exception {
        when(resetCodeService.extractTokenValue(resetCode)).thenReturn(tokenValue);
        when(userManager.loadUserByToken(tokenValue)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        doNothing().when(userManager).setNewPassword(user, newPassword);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/change")
                .param("password", newPassword)
                .param("matchingPassword", newPassword)
                .flashAttr("resetCode", resetCode))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", PASSWORD_CHANGED_MESSAGE))
                .andExpect(view().name("login"));

        verify(resetCodeService, times(1)).extractTokenValue(resetCode);
        verify(userManager, times(1)).loadUserByToken(tokenValue);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userManager, times(1)).setNewPassword(user, newPassword);
    }

    @Test
    void shouldNotProcessPasswordChange() throws Exception {
        when(resetCodeService.extractTokenValue(resetCode)).thenReturn(tokenValue);
        when(userManager.loadUserByToken(tokenValue)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/change")
                .param("password", newPassword)
                .param("matchingPassword", newPassword)
                .flashAttr("resetCode", resetCode))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", PASSWORD_CHANGED_MESSAGE))
                .andExpect(view().name("login"));

        verify(resetCodeService, times(1)).extractTokenValue(resetCode);
        verify(userManager, times(1)).loadUserByToken(tokenValue);
        verifyNoMoreInteractions(userManager);
        verifyNoInteractions(passwordEncoder);
    }
}