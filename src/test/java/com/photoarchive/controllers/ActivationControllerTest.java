package com.photoarchive.controllers;

import com.photoarchive.PhotoarchiveApplication;
import com.photoarchive.domain.Token;
import com.photoarchive.managers.TokenManager;
import com.photoarchive.managers.UserManager;
import com.photoarchive.services.ActivationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ActivationController.class)
class ActivationControllerTest {

    private final String VALID_TOKEN_VALUE = "value";
    private final String INVALID_TOKEN_VALUE = "noValue";

    @MockBean
    private ActivationService activationService;

    @MockBean
    private TokenManager tokenManager;

    @MockBean
    private UserManager userManager;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext applicationContext;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    @Test
    void shouldCallActivationServiceAndReturnLoginViewWithActivatedMessageWhenParamTokenIsValid() throws Exception {
        //given
        Token foundToken = new Token();
        when(tokenManager.findTokenByValue(VALID_TOKEN_VALUE))
                .thenReturn(Optional.of(foundToken));
        doNothing().when(activationService).activate(foundToken);
        //when //then
        mockMvc.perform(MockMvcRequestBuilders.get("/activate")
                .param("value", VALID_TOKEN_VALUE))
                .andExpect(model().attribute("message", ReflectionTestUtils.getField(ActivationController.class, "ACCOUNT_ACTIVATED_MESSAGE")))
                .andExpect(view().name("login"))
                .andExpect(status().isOk());

        verify(activationService,times(1)).activate(foundToken);
    }

    @Test
    void shouldNotCallActivationServiceAndReturnLoginViewWithNotActivatedMessageWhenParamTokenIsInvalid() throws Exception {
        //given
        Token notFoundToken = new Token();
        when(tokenManager.findTokenByValue(INVALID_TOKEN_VALUE))
                .thenReturn(Optional.empty());
        doNothing().when(activationService).activate(notFoundToken);
        //when //then
        mockMvc.perform(MockMvcRequestBuilders.get("/activate")
                .param("value", INVALID_TOKEN_VALUE))
                .andExpect(model().attribute("message", ReflectionTestUtils.getField(ActivationController.class, "ACCOUNT_NOT_ACTIVATED")))
                .andExpect(view().name("info-page"))
                .andExpect(status().isOk());

        verifyNoInteractions(activationService);
    }
}