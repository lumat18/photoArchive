package com.photoarchive.services;

import com.photoarchive.domain.Photo;
import com.photoarchive.exceptions.UploadFileFailureException;
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
    private PhotoSetUpService photoSetUpService;

    @Autowired
    public UploadService(PhotoRepository photoRepository,
                         TagRepository tagRepository,
                         PhotoSetUpService photoSetUpService) {
        this.photoRepository = photoRepository;
        this.tagRepository = tagRepository;
        this.photoSetUpService = photoSetUpService;
    }

    public Photo addPhoto(final PhotoWithUrlDTO photoWithUrlDTO) {
        Photo photo = new Photo();
        photo.setUrl(photoWithUrlDTO.getUrl());
        photoSetUpService.setCorrectTags(photo, photoWithUrlDTO.getTagsAsString());

        return saveToDB(photo);
    }

    public Photo addPhoto(final PhotoWithFileDTO photoWithFileDTO) throws UploadFileFailureException {
        Photo photo = new Photo();
        photoSetUpService.setCorrectUrl(photo, photoWithFileDTO.getMultipartFile());
        photoSetUpService.setCorrectTags(photo, photoWithFileDTO.getTagsAsString());

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
