package com.photoarchive.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PhotoDTO {

    private String url;
    @NotBlank(message = "please set minimum 1 tag")
    private String tagsAsString;

}
