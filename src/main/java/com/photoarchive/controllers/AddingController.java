package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.Tag;
import com.photoarchive.models.PhotoDTO;
import com.photoarchive.repositories.PhotoRepository;
import com.photoarchive.services.PhotoAddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AddingController {

    private PhotoAddingService photoAddingService;

    @Autowired
    public AddingController(PhotoAddingService photoAddingService) {
        this.photoAddingService = photoAddingService;
    }

    @GetMapping("/adding")
    public String show(Model model){
        model.addAttribute("photoDTO", new PhotoDTO());
        return "adding";
    }

    @PostMapping("/upload-photo")
    public String processPost(@ModelAttribute PhotoDTO photoDTO){
        System.out.println(photoDTO);
        System.out.println("im in processPost");
        photoAddingService.addPhoto(photoDTO);
        return "redirect:/adding";
    }
}
