package com.photoarchive.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PhotoDTO {

    @URL(message = "please set a valid url")
    @NotBlank(message = "please set an url")
    @Size(max = 255, message = "url is too long")
    private String url;
    @NotBlank(message = "please set minimum 1 tag")
    private String tagsAsString;

}
