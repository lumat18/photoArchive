package com.photoarchive.models;

import com.photoarchive.annotations.PhotoFile;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PhotoWithFileDTO {

    @PhotoFile
    private MultipartFile multipartFile;
    @NotBlank(message = "Please set minimum 1 tag")
    private String tagsAsString;
}
