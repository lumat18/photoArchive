package com.photoarchive.controllers;

import com.photoarchive.domain.Photo;
import com.photoarchive.exceptions.UnsupportedPhotoFormatException;
import com.photoarchive.models.UnsupportedPhotoFormatMessage;
import com.photoarchive.services.PhotoAddingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class AddingController {

    private PhotoAddingService photoAddingService;

    @ModelAttribute(name = "message")
    private UnsupportedPhotoFormatMessage message(){
        return new UnsupportedPhotoFormatMessage();
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
    public String processPostWithUrl(@RequestParam(name = "url") String url, @RequestParam(name = "tags") String tags){
        photoAddingService.addPhoto(url, tags);
        return "redirect:/adding";
    }

    @PostMapping("/upload-photo-with-file")
    public String processPostWithFile(@RequestParam(name = "file") MultipartFile multipartFile,
                                      @RequestParam(name = "tags") String tags,
                                      @ModelAttribute UnsupportedPhotoFormatMessage errorMessage,
                                      RedirectAttributes redirectAttributes
                                      ){
        try {
            photoAddingService.addPhoto(multipartFile, tags);
        } catch (UnsupportedPhotoFormatException e) {
            log.warn(e.getMessage());
            errorMessage.setActive(true);
            redirectAttributes.addFlashAttribute("message", errorMessage);
        }finally {
            return "redirect:/adding";
        }
    }
}
