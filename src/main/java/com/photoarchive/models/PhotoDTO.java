package com.photoarchive.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhotoDTO {

    private String url;
    private String tagsAsString;

}
