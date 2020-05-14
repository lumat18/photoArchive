package com.photoarchive.controllers;

import com.photoarchive.domain.User;
import com.photoarchive.exceptions.UserAlreadyExistsException;
import com.photoarchive.managers.UserManager;
import com.photoarchive.models.UserDTO;
import com.photoarchive.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {
    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private UserManager userManager;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Captor
    private ArgumentCaptor<User> captor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldShowRegistrationPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(view().name("registration"));
    }

    @Test
    void shouldProcessRegistration() throws Exception {
        //given
        final UserDTO userDTO = properUserDTO();
        final User user = User.builder().username(userDTO.getUsername()).build();
        when(userManager.createUser(any())).thenReturn(user);
        doNothing().when(registrationService).register(user);
        //when
        mockMvc.perform(post("/register")
                .param("username", userDTO.getUsername())
                .param("email", userDTO.getEmail())
                .param("firstName", userDTO.getFirstName())
                .param("surname", userDTO.getSurname())
                .param("password", userDTO.getPassword())
                .param("matchingPassword", userDTO.getMatchingPassword()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", ReflectionTestUtils
                        .getField(RegistrationController.class, "ACTIVATION_LINK_SENT_MESSAGE")))
                .andExpect(view().name("info-page"));
        verify(registrationService).register(captor.capture());
        //then
        assertThat(captor.getValue().getUsername()).isEqualTo(userDTO.getUsername());

    }

    private UserDTO properUserDTO() {
        final UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setEmail("email@email.com");
        userDTO.setFirstName("firstName");
        userDTO.setSurname("surname");
        userDTO.setPassword("password1");
        userDTO.setMatchingPassword(userDTO.getPassword());
        return userDTO;
    }

    @Test
    void shouldNotProcessRegistrationWhenUserAlreadyExists() throws Exception {
        //given
        final UserDTO userDTO = alreadyExistingUserDto();
        final User user = User.builder().username(userDTO.getUsername()).build();
        when(userManager.createUser(any())).thenReturn(user);
        doThrow(new UserAlreadyExistsException()).when(registrationService).register(any());
        //when then
        mockMvc.perform(post("/register")
                .param("username", userDTO.getUsername())
                .param("email", userDTO.getEmail())
                .param("firstName", userDTO.getFirstName())
                .param("surname", userDTO.getSurname())
                .param("password", userDTO.getPassword())
                .param("matchingPassword", userDTO.getMatchingPassword()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userAlreadyExists"))
                .andExpect(view().name("registration"));
    }

    private UserDTO alreadyExistingUserDto() {
        return properUserDTO();
    }

    @Test
    void shouldReturnErrorsWhenEmailIsNotValid() throws Exception {
        final UserDTO userDTO = notValidEmailUserDTO();
        final User user = User.builder().username(userDTO.getUsername()).build();
        when(userManager.createUser(any())).thenReturn(user);
        mockMvc.perform(post("/register")
                .param("username", userDTO.getUsername())
                .param("email", userDTO.getEmail())
                .param("firstName", userDTO.getFirstName())
                .param("surname", userDTO.getSurname())
                .param("password", userDTO.getPassword())
                .param("matchingPassword", userDTO.getMatchingPassword()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("registration"));

        verifyNoInteractions(registrationService);
    }

    private UserDTO notValidEmailUserDTO() {
        final UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setEmail("emailemailcom");
        userDTO.setFirstName("firstName");
        userDTO.setSurname("surname");
        userDTO.setPassword("password1");
        userDTO.setMatchingPassword(userDTO.getPassword());
        return userDTO;
    }

    @Test
    void shouldReturnErrorsWhenPasswordIsNotStrongEnough() throws Exception {
        final UserDTO userDTO = notValidPasswordUserDTO();
        final User user = User.builder().username(userDTO.getUsername()).build();
        when(userManager.createUser(any())).thenReturn(user);
        mockMvc.perform(post("/register")
                .param("username", userDTO.getUsername())
                .param("email", userDTO.getEmail())
                .param("firstName", userDTO.getFirstName())
                .param("surname", userDTO.getSurname())
                .param("password", userDTO.getPassword())
                .param("matchingPassword", userDTO.getMatchingPassword()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("registration"));

        verifyNoInteractions(registrationService);
    }

    private UserDTO notValidPasswordUserDTO() {
        final UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setEmail("email@email.com");
        userDTO.setFirstName("firstName");
        userDTO.setSurname("surname");
        userDTO.setPassword("aaa");
        userDTO.setMatchingPassword(userDTO.getPassword());
        return userDTO;
    }

    @Test
    void shouldReturnErrorsWhenPasswordIsNotMatching() throws Exception {
        final UserDTO userDTO = notMatchingPasswordUserDTO();
        final User user = User.builder().username(userDTO.getUsername()).build();
        when(userManager.createUser(any())).thenReturn(user);
        mockMvc.perform(post("/register")
                .param("username", userDTO.getUsername())
                .param("email", userDTO.getEmail())
                .param("firstName", userDTO.getFirstName())
                .param("surname", userDTO.getSurname())
                .param("password", userDTO.getPassword())
                .param("matchingPassword", userDTO.getMatchingPassword()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("registration"));

        verifyNoInteractions(registrationService);
    }

    private UserDTO notMatchingPasswordUserDTO() {
        final UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setEmail("email@email.com");
        userDTO.setFirstName("firstName");
        userDTO.setSurname("surname");
        userDTO.setPassword("password1");
        userDTO.setMatchingPassword("password2");
        return userDTO;
    }
}