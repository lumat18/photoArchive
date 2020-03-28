package com.photoarchive.services;

import com.photoarchive.domain.Photo;
import com.photoarchive.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SearchService {
    private PhotoRepository photoRepository;

    @Autowired
    public SearchService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Set<Photo> getPhotosByTags(String tags){
        final String[] s = tags.split(" ");

    }
}
