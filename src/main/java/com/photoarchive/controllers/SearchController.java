package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.services.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/search")
public class SearchController {

    private SearchService searchService;
    private Set<Photo> foundPhotos;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public String getPhotos(Model model){
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
