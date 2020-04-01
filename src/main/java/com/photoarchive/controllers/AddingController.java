package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.exceptions.IncorrectUrlFormatException;
import com.photoarchive.exceptions.UnsupportedPhotoFormatException;
import com.photoarchive.models.InvalidInputMessage;
import com.photoarchive.models.PhotoDTO;
import com.photoarchive.services.PhotoAddingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Slf4j
public class AddingController {

    private PhotoAddingService photoAddingService;

    @ModelAttribute(name = "photoDTO")
    private PhotoDTO photoDTO(){
        return new PhotoDTO();
    }

    @ModelAttribute(name = "invalidSource")
    private InvalidInputMessage invalidSource(){
        return new InvalidInputMessage();
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
    public String processPostWithUrl(
                                     @Valid PhotoDTO photoDTO,
                                     Errors errors,
                                     @ModelAttribute InvalidInputMessage invalidSource,
                                     RedirectAttributes redirectAttributes
                                     ){
        if (errors.hasErrors()){
            return "adding";
        }
        try {
            photoAddingService.addPhoto(photoDTO.getUrl(), photoDTO.getTagsAsString());
        } catch (IncorrectUrlFormatException e) {
            log.warn(e.getMessage());
            invalidSource.activate("Invalid url format");
            redirectAttributes.addFlashAttribute("invalidSource", invalidSource);
        }
            return "redirect:/adding";

    }

    @PostMapping("/upload-photo-with-file")
    public String processPostWithFile(@RequestParam(name = "file") MultipartFile multipartFile,
                                      @RequestParam(name = "tags") String tags,
                                      @ModelAttribute InvalidInputMessage invalidSource,
                                      RedirectAttributes redirectAttributes
                                      ){
        try {
            photoAddingService.addPhoto(multipartFile, tags);
        } catch (UnsupportedPhotoFormatException e) {
            log.warn(e.getMessage());
            invalidSource.activate("Unsupported file format");
            redirectAttributes.addFlashAttribute("invalidSource", invalidSource);
        }finally {
            return "redirect:/adding";
        }
    }
}
