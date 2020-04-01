package com.photoarchive.controllers;

import com.photoarchive.models.PhotoWithFileDTO;
import com.photoarchive.models.PhotoWithUrlDTO;
import com.photoarchive.services.PhotoAddingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
public class AddingController {

    private PhotoAddingService photoAddingService;

    @ModelAttribute(name = "photoWithUrlDTO")
    private PhotoWithUrlDTO photoWithUrlDTO(){
        return new PhotoWithUrlDTO();
    }

    @ModelAttribute(name = "photoWithFileDTO")
    private PhotoWithFileDTO photoWithFileDTO(){
        return new PhotoWithFileDTO();
    }

    @Autowired
    public AddingController(PhotoAddingService photoAddingService) {
        this.photoAddingService = photoAddingService;
    }

    @GetMapping("/adding")
    public String show(Model model){
        return "adding";
    }

    @PostMapping("/upload-photo-with-url")
    public String processPostWithUrl(@Valid PhotoWithUrlDTO photoWithUrlDTO, Errors errors){
        if (errors.hasErrors()){
            return "adding";
        }
        photoAddingService.addPhoto(photoWithUrlDTO);
        return "redirect:/adding";
    }

    @PostMapping("/upload-photo-with-file")
    public String processPostWithFile(@Valid PhotoWithFileDTO photoWithFileDTO, Errors errors){
        if (errors.hasErrors()){
            return "adding";
        }
            photoAddingService.addPhoto(photoWithFileDTO);
            return "redirect:/adding";
    }
}
