package com.photoarchive.services;

import com.photoarchive.domain.Tag;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class TagParsingService {

    public Set<Tag> parseTagSet(String tagsString) {

        String[] splitTagArray = tagsString.split(" ");
        return convertArrayToSet(splitTagArray);
    }

    private Set<Tag> convertArrayToSet(String[] array) {
        Set<Tag> tags = new HashSet<>();
        Arrays.stream(array).forEach(tagString -> {
            Tag tag = new Tag();
            tag.setName(tagString);
            tags.add(tag);
        });
        return tags;
    }
}
