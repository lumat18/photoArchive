package com.photoarchive.controllers;

import com.photoarchive.domain.User;
import com.photoarchive.managers.UserManager;
import com.photoarchive.messageCreation.MessageType;
import com.photoarchive.services.EmailService;
import com.photoarchive.services.ResetLinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResetLinkController.class)
class ResetLinkControllerTest {

    private static final String LINK_SENT_MESSAGE = "Reset link was sent";
    private static final String LINK_NOT_SENT_MESSAGE = "Account does not exist or is not activated";

    @MockBean
    private ResetLinkService resetLinkService;
    @MockBean
    private UserManager userManager;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        user = new User();
        when(userManager.loadUserByEmail("email")).thenReturn(Optional.of(user));
    }

    @Test
    void shouldShowEmailInputPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/reset"))
                .andExpect(status().isOk())
                .andExpect(view().name("email-input"));
        verifyNoInteractions(resetLinkService, userManager);
    }

    @Test
    void shouldNotSendMessageToNotExistingUser() throws Exception {
        when(userManager.loadUserByEmail("email")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/reset")
                .param("email", "email"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", LINK_NOT_SENT_MESSAGE))
                .andExpect(view().name("email-input"));
        verify(userManager, times(1)).loadUserByEmail("email");
        verifyNoInteractions(resetLinkService);
    }

    @Test
    void shouldNotSendMessageToNotEnabledUser() throws Exception {
        user.setEnabled(false);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/reset")
                .param("email", "email"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", LINK_NOT_SENT_MESSAGE))
                .andExpect(view().name("email-input"));
        verify(userManager, times(1)).loadUserByEmail("email");
        verifyNoInteractions(resetLinkService);
    }

    @Test
    void shouldSendResetLinkMessage() throws Exception {
        user.setEnabled(true);
        doNothing().when(resetLinkService).sendPasswordResetMessageTo(user);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/reset")
                .param("email", "email"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", LINK_SENT_MESSAGE))
                .andExpect(view().name("info-page"));
        verify(userManager, times(1)).loadUserByEmail("email");
        verify(resetLinkService, times(1)).sendPasswordResetMessageTo(user);
    }

}