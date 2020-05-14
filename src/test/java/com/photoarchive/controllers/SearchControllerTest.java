package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.User;
import com.photoarchive.managers.UserManager;
import com.photoarchive.models.UserInfo;
import com.photoarchive.services.SearchService;
import com.photoarchive.testUtils.MockSpringSecurityFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SearchController.class)
class SearchControllerTest {
    @MockBean
    private SearchService searchService;
    @MockBean
    private UserManager userManager;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    @Test
    void showSearchPage() throws Exception {
        final User user = new User();
        user.setFirstName("testName");

        mockMvc.perform(get("/search")
                .principal(new UsernamePasswordAuthenticationToken(user, null)))
                .andExpect(model().attributeExists("userInfo"))
                .andExpect(model()
                        .attribute("userInfo",
                                hasProperty("firstName", equalTo(user.getFirstName()))))
                .andExpect(view().name("search"));
    }

    @Test
    void shouldShowFoundPhotos() throws Exception {
        final String tagString = "tag1+tag2";
        final Photo photo = new Photo();
        when(searchService.getPhotosByTags(tagString)).thenReturn(Set.of(photo));

        mockMvc.perform(get("/search/find-photos")
                .param("tagString", tagString)
                .sessionAttr("userInfo", new UserInfo()))
                .andExpect(model().attribute("foundPhotos", hasSize(1)))
                .andExpect(model().attribute("foundPhotos", hasItem(photo)))
                .andExpect(view().name("search"));
    }

    @Test
    void shouldNotShowPhotosWhenNoPhotosAreFound() throws Exception {
        final String tagString = "tag1+tag2";
        when(searchService.getPhotosByTags(tagString)).thenReturn(Set.of());

        mockMvc.perform(get("/search/find-photos")
                .param("tagString", tagString)
                .sessionAttr("userInfo", new UserInfo()))
                .andExpect(model().attribute("foundPhotos", hasSize(0)))
                .andExpect(view().name("search"));
    }
}