package com.photoarchive.services;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.Tag;
import com.photoarchive.repositories.PhotoRepository;
import com.photoarchive.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class PhotoAddingService {

    private PhotoRepository photoRepository;
    private TagRepository tagRepository;
    private TagParsingService tagParsingService;

    @Autowired
    public PhotoAddingService(PhotoRepository photoRepository, TagRepository tagRepository, TagParsingService tagParsingService) {
        this.photoRepository = photoRepository;
        this.tagRepository = tagRepository;
        this.tagParsingService = tagParsingService;
    }

    @Transactional
    public Photo addPhoto(String url, String tags){
        Photo photo = new Photo();
        photo.setUrl(url);
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

        tagRepository.saveAll(photo.getTags());
        tagRepository.flush();
        return photoRepository.saveAndFlush(photo);
    }
}
