package com.photoarchive.services;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.Tag;
import com.photoarchive.repositories.PhotoRepository;
import com.photoarchive.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class SearchService {
    private PhotoRepository photoRepository;
    private TagRepository tagRepository;
    private TagParsingService tagParsingService;

    @Autowired
    public SearchService(PhotoRepository photoRepository, TagRepository tagRepository, TagParsingService tagParsingService) {
        this.photoRepository = photoRepository;
        this.tagRepository = tagRepository;
        this.tagParsingService = tagParsingService;
    }

    public Set<Photo> getPhotosByTags(String tagsAsString){
        final Set<Tag> incomingTagSet = tagParsingService.parseTagSet(tagsAsString);
        Set<Tag> queryTagSet = new HashSet<>();

        incomingTagSet.forEach(tag -> {
            final Optional<Tag> tagOptional = tagRepository.findOne(Example.of(tag));
            tagOptional.ifPresent(queryTagSet::add);
        });

        return photoRepository.findByTagsIn(queryTagSet);
    }
}
