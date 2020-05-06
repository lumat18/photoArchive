package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.User;
import com.photoarchive.services.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/search")
@SessionAttributes("userInfo")
public class SearchController {

    private SearchService searchService;
    private Set<Photo> foundPhotos;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public String getPhotos(Model model, @AuthenticationPrincipal User user){
        UserInfo userInfo = UserInfo.createUserInfo(user);
        System.out.println("userInfo = " + userInfo);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("foundPhotos", foundPhotos);
        return "search";
    }

    @PostMapping("/find-photos")
    public String processSearch(@RequestParam(name = "tagString") String tagString){
        foundPhotos = searchService.getPhotosByTags(tagString);
        log.info("Photo found in database: " + foundPhotos);

        return  "redirect:/search";
    }
}
