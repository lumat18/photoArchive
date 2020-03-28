package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.repositories.PhotoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    List<Photo> photos = new ArrayList<>();

    @GetMapping("/search")
    public String getPhotos(Model model){
        model.addAttribute("tagString", photos);
        return "home";
    }

    @PostMapping("/seach")
    public String processSearch(@ModelAttribute String tagString){

        return  "redirect:/home";
    }
}
