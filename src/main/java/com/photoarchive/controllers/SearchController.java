package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class SearchController {
    private SearchService searchService;

    private String tagString;

    private Set<Photo> foundPhotos;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/getSearch")
    public String getPhotos(Model model){
        model.addAttribute("tagString", tagString);
        model.addAttribute("foundPhotos", foundPhotos);
        return "home";
    }

    @PostMapping("/search")
    public String processSearch(@RequestParam(name = "tagString") String tagString){
        System.out.println("searchService.getPhotosByTags(tagString) = "
                + searchService.getPhotosByTags(tagString));

        foundPhotos = searchService.getPhotosByTags(tagString);
        return  "redirect:/getSearch";
    }
}
