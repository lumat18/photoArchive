package com.photoarchive.services;

import com.photoarchive.domain.Photo;
import com.photoarchive.models.PhotoWithFileDTO;
import com.photoarchive.models.PhotoWithUrlDTO;
import com.photoarchive.repositories.PhotoRepository;
import com.photoarchive.repositories.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class UploadService {

    private PhotoRepository photoRepository;
    private TagRepository tagRepository;
    private PhotoFieldsSetUpService photoFieldsSetUpService;

    @Autowired
    public UploadService(PhotoRepository photoRepository,
                         TagRepository tagRepository,
                         PhotoFieldsSetUpService photoFieldsSetUpService) {
        this.photoRepository = photoRepository;
        this.tagRepository = tagRepository;
        this.photoFieldsSetUpService = photoFieldsSetUpService;
    }

    public Photo addPhoto(final PhotoWithUrlDTO photoWithUrlDTO) {
        Photo photo = new Photo();
        photo.setUrl(photoWithUrlDTO.getUrl());
        photoFieldsSetUpService.setCorrectTags(photo, photoWithUrlDTO.getTagsAsString());

        return saveToDB(photo);
    }

    public Photo addPhoto(final PhotoWithFileDTO photoWithFileDTO) {
        Photo photo = new Photo();
        photoFieldsSetUpService.setCorrectUrl(photo, photoWithFileDTO.getMultipartFile());
        photoFieldsSetUpService.setCorrectTags(photo, photoWithFileDTO.getTagsAsString());

        return saveToDB(photo);
    }

    @Transactional
    public Photo saveToDB(Photo photo){
        tagRepository.saveAll(photo.getTags());
        tagRepository.flush();
        log.info("Photo added to database: " + photo);
        return photoRepository.saveAndFlush(photo);
    }
}
