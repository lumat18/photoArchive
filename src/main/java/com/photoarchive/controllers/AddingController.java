package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.services.PhotoAddingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class AddingController {

    private PhotoAddingService photoAddingService;

    @Autowired
    public AddingController(PhotoAddingService photoAddingService) {
        this.photoAddingService = photoAddingService;
    }

    @GetMapping("/adding")
    public String show(){
        return "adding";
    }

    @PostMapping("/upload-photo-with-url")
    public String processPostWithUrl(@RequestParam(name = "url") String url, @RequestParam(name = "tags") String tags){
        final Photo photo = photoAddingService.addPhoto(url, tags);
        log.info("Photo added to database: " + photo);
        return "redirect:/adding";
    }

    @PostMapping("/upload-photo-with-file")
    public String processPostWithFile(@RequestParam(name = "file") MultipartFile multipartFile, @RequestParam(name = "tags") String tags){
        final Photo photo = photoAddingService.addPhoto(multipartFile, tags);
        log.info("Photo added to database: " + photo);
        return "redirect:/adding";
    }
}
