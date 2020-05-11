package com.photoarchive.controllers;

import com.photoarchive.managers.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.web.WebAttributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

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
    void shouldReturnLoginPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(view().name("login"))
                .andExpect(status().isOk());
    }
    @Test
    void shouldSetUserNotActiveMessageIfSecurityThrowsDisabledException() throws Exception {
        DisabledException disabledException = new DisabledException("");

        mockMvc.perform(MockMvcRequestBuilders.get("/login-error")
                .sessionAttr(WebAttributes.AUTHENTICATION_EXCEPTION, disabledException))
                .andExpect(model().attribute("message",LoginController.USER_NOT_ACTIVE_MESSAGE))
                .andExpect(view().name("login"));
    }
    @Test
    void shouldSetBadCredentialsMessageIfSecurityThrowsBadCredentialsException() throws Exception {
        BadCredentialsException badCredentialsException = new BadCredentialsException("");

        mockMvc.perform(MockMvcRequestBuilders.get("/login-error")
                .sessionAttr(WebAttributes.AUTHENTICATION_EXCEPTION, badCredentialsException))
                .andExpect(model().attribute("message",LoginController.BAD_CREDENTIALS_MESSAGE))
                .andExpect(view().name("login"));
    }
}