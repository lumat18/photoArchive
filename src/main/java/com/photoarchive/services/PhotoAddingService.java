package com.photoarchive.services;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.Tag;
import com.photoarchive.models.PhotoDTO;
import com.photoarchive.repositories.PhotoRepository;
import com.photoarchive.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class PhotoAddingService {

    private PhotoRepository photoRepository;
    private TagRepository tagRepository;

    @Autowired
    public PhotoAddingService(PhotoRepository photoRepository, TagRepository tagRepository) {
        this.photoRepository = photoRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Photo addPhoto(PhotoDTO photoDTO){
//
//        Tag pies = new Tag();
//        pies.setName("pies");
//        Tag kot = new Tag();
//        kot.setName("kot");
//
//        Photo photo = new Photo();
//        photo.setUrl("http");
//        photo.setTags(Set.of(pies,kot));
//
//        tagRepository.save(pies);
//        tagRepository.save(kot);
//
//        tagRepository.flush();
//
//        return photoRepository.save(photo);
        String[] split = photoDTO.getTagsAsString().split(" ");
        List<String> strings = new ArrayList<>();
        for (String splited : split) {
            strings.add(splited);
        }
        Set<Tag> tags = new HashSet<>();
        strings.forEach(string -> {
            Tag tag = new Tag();
            tag.setName(string);
            tags.add(tag);
        });

        Photo photo = new Photo();
        photo.setUrl(photoDTO.getUrl());
        photo.setTags(tags);
        
        tagRepository.saveAll(tags);
        tagRepository.flush();
        return photoRepository.saveAndFlush(photo);
    }
}
