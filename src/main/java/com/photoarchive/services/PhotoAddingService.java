package com.photoarchive.services;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.Tag;
import com.photoarchive.exceptions.UnsupportedPhotoFormatException;
import com.photoarchive.repositories.PhotoRepository;
import com.photoarchive.repositories.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class PhotoAddingService {

    private PhotoRepository photoRepository;
    private TagRepository tagRepository;
    private TagParsingService tagParsingService;
    private CloudinaryService cloudinaryService;
    private PhotoValidatingService photoValidatingService;

    @Autowired
    public PhotoAddingService(PhotoRepository photoRepository, TagRepository tagRepository, TagParsingService tagParsingService, CloudinaryService cloudinaryService, PhotoValidatingService photoValidatingService) {
        this.photoRepository = photoRepository;
        this.tagRepository = tagRepository;
        this.tagParsingService = tagParsingService;
        this.cloudinaryService = cloudinaryService;
        this.photoValidatingService = photoValidatingService;
    }

    public Photo addPhoto(String url, String tags){
        Photo photo = new Photo();
        setCorrectTags(photo, tags);
        photo.setUrl(url);

        return saveToDB(photo);
    }

    public Photo addPhoto(MultipartFile multipartFile, String tags) throws UnsupportedPhotoFormatException {
        photoValidatingService.isValid(multipartFile);

        Photo photo = new Photo();
        setCorrectTags(photo, tags);
        setCorrectUrl(multipartFile, photo);

        return saveToDB(photo);
    }

    private void setCorrectTags(Photo photo, String tags) {
        final Set<Tag> tagsFromInput = tagParsingService.parseTagSet(tags);

        Set<Tag> tagsToAdd = new HashSet<>();

        tagsFromInput.forEach(tag -> {
            Optional<Tag> one = tagRepository.findOne(Example.of(tag));
            if (one.isPresent()) {
                tagsToAdd.add(one.get());
            } else {
                tagsToAdd.add(tag);
            }
        });
        photo.setTags(tagsToAdd);
    }

    private void setCorrectUrl(MultipartFile multipartFile, Photo photo) {
        Map result = cloudinaryService.upload(multipartFile);
        String urlInCloud = (String) result.get("url");
        System.out.println(urlInCloud);
        photo.setUrl(urlInCloud);
    }

    @Transactional
    public Photo saveToDB(Photo photo){
        tagRepository.saveAll(photo.getTags());
        tagRepository.flush();
        log.info("Photo added to database: " + photo);
        return photoRepository.saveAndFlush(photo);
    }
}
