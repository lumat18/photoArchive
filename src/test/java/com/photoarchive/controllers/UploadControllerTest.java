package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.managers.UserManager;
import com.photoarchive.models.PhotoWithFileDTO;
import com.photoarchive.models.PhotoWithUrlDTO;
import com.photoarchive.models.UserInfo;
import com.photoarchive.services.UploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UploadController.class)
class UploadControllerTest {

    @MockBean
    private UploadService uploadService;
    @MockBean
    private UserManager userManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Captor
    private ArgumentCaptor<PhotoWithUrlDTO> captorUrl;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void shouldReturnUploadPage() throws Exception {
        mockMvc.perform(get("/upload")
                .sessionAttr("userInfo", new UserInfo()))
                .andExpect(view().name("upload"));
    }

    @Test
    void shouldProcessPostWithUrl() throws Exception {
        String validImageUrl = "https://www.coca-cola.com/content/dam/brands/tw/coca-cola/image/coke-logo.png";
        when(uploadService.addPhoto((PhotoWithFileDTO) any())).thenReturn(new Photo());

        mockMvc.perform(post("/upload/photo-with-url")
                .sessionAttr("userInfo", new UserInfo())
                .param("url", validImageUrl)
                .param("tagsAsString", "tag+tag"))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("upload"));

        verify(uploadService).addPhoto(captorUrl.capture());
        assertThat(captorUrl.getValue().getUrl()).isEqualTo(validImageUrl);
    }

    @Test
    void shouldNotProcessPostWithUrlWhenValidationProducesErrors() throws Exception {
        String invalidImageUrl = "invalid";

        mockMvc.perform(post("/upload/photo-with-url")
                .sessionAttr("userInfo", new UserInfo())
                .param("url", invalidImageUrl)
                .param("tagsAsString", ""))
                .andExpect(model().hasErrors())
                .andExpect(view().name("upload"));

        verifyNoInteractions(uploadService);
    }
}