package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.User;
import com.photoarchive.models.UserInfo;
import com.photoarchive.services.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/search")
@SessionAttributes({"userInfo", "foundPhotos"})
public class SearchController {

    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public String getPhotos(@AuthenticationPrincipal User user, Model model){
        UserInfo userInfo = UserInfo.createUserInfo(user);
        model.addAttribute("userInfo", userInfo);
        return "search";
    }

    @PostMapping("/find-photos")
    public String processSearch(@RequestParam(name = "tagString") String tagString, Model model){
        Set<Photo> foundPhotos = searchService.getPhotosByTags(tagString);
        model.addAttribute("foundPhotos", foundPhotos);
        log.info("Photo found in database: " + foundPhotos);
        return  "search";
    }
}
