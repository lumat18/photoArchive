package com.photoarchive.services;

import com.photoarchive.domain.Photo;
import com.photoarchive.exceptions.UploadFileFailureException;
import com.photoarchive.models.PhotoWithFileDTO;
import com.photoarchive.models.PhotoWithUrlDTO;
import com.photoarchive.repositories.PhotoRepository;
import com.photoarchive.repositories.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@SpringBootTest
class UploadServiceTest {
    @MockBean
    private PhotoRepository photoRepository;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private PhotoSetUpService photoSetUpService;

    @Autowired
    private UploadService uploadService;

    @Test
    void shouldAddPhotoWithUrl() {
        final String tags = "";
        final Photo photo = new Photo();
        when(photoSetUpService.setCorrectTags(photo, tags)).thenReturn(photo);
        when(tagRepository.saveAll(photo.getTags())).thenReturn(List.of());
        doNothing().when(tagRepository).flush();
        when(photoRepository.saveAndFlush(photo)).thenReturn(photo);

        final PhotoWithUrlDTO photoWithUrlDTO = new PhotoWithUrlDTO();
        photoWithUrlDTO.setTagsAsString(tags);
        final Photo addedPhoto = uploadService.addPhoto(photoWithUrlDTO);

        assertThat(addedPhoto.getUrl()).isNull();
        assertThat(addedPhoto.getTags()).isEmpty();
        verify(photoSetUpService, times(1)).setCorrectTags(photo, tags);
        verify(tagRepository).saveAll(photo.getTags());
        verify(tagRepository).flush();
        verify(photoRepository).saveAndFlush(photo);
    }

    @Test
    void shouldAddPhotoWithFile() throws UploadFileFailureException {
        final String tags = "";
        final MultipartFile file = new MockMultipartFile("file", "file".getBytes());
        final Photo photo = new Photo();

        when(photoSetUpService.setCorrectUrl(photo, file)).thenReturn(photo);

        when(photoSetUpService.setCorrectTags(photo, tags)).thenReturn(photo);
        when(tagRepository.saveAll(photo.getTags())).thenReturn(List.of());
        doNothing().when(tagRepository).flush();
        when(photoRepository.saveAndFlush(photo)).thenReturn(photo);

        final PhotoWithFileDTO photoWithFileDTO = new PhotoWithFileDTO();
        photoWithFileDTO.setMultipartFile(file);
        photoWithFileDTO.setTagsAsString(tags);
        final Photo addedPhoto = uploadService.addPhoto(photoWithFileDTO);

        assertThat(addedPhoto.getUrl()).isNull();
        assertThat(addedPhoto.getTags()).isEmpty();
        verify(photoSetUpService, times(1)).setCorrectUrl(photo, file);
        verify(photoSetUpService, times(1)).setCorrectTags(photo, tags);
        verify(tagRepository).saveAll(photo.getTags());
        verify(tagRepository).flush();
        verify(photoRepository).saveAndFlush(photo);
    }

    @Test
    void shouldThrowWhenFailToSetUrlForPhoto() throws UploadFileFailureException {
        final MultipartFile file = new MockMultipartFile("file", "file".getBytes());
        final Photo photo = new Photo();
        when(photoSetUpService.setCorrectUrl(photo, file)).thenThrow(UploadFileFailureException.class);

        assertThatExceptionOfType(UploadFileFailureException.class)
                .isThrownBy(() -> photoSetUpService.setCorrectUrl(photo, file));
        verify(photoSetUpService, times(1)).setCorrectUrl(photo, file);
        verifyNoInteractions(tagRepository, photoRepository);
    }
}