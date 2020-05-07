package com.photoarchive.models;

import com.photoarchive.annotations.PhotoURL;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PhotoWithUrlDTO {

    @NotBlank(message = "Please set an url. ")
    @Size(max = 255, message = "Url is too long. ")
    @PhotoURL
    private String url;
    @NotBlank(message = "Please set minimum 1 tag. ")
    private String tagsAsString;

}
