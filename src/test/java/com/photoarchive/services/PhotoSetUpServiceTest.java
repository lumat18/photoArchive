package com.photoarchive.services;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.Tag;
import com.photoarchive.exceptions.UploadFileFailureException;
import com.photoarchive.repositories.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoSetUpServiceTest {

    @Mock
    private TagParsingService tagParsingService;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private CloudinaryService cloudinaryService;
    @InjectMocks
    private PhotoSetUpService photoSetUpService;

    private Photo SAMPLE_PHOTO_WITH_NO_TAGS;
    private Tag EXISTING_TAG;
    private Tag NEW_TAG;

    @BeforeEach
    void setUp() {
        this.SAMPLE_PHOTO_WITH_NO_TAGS = new Photo();
        this.EXISTING_TAG = new Tag();
        EXISTING_TAG.setName("tag1");
        this.NEW_TAG = new Tag();
        NEW_TAG.setName("tag2");
    }

    @Test
    void shouldAddTagThatExistsInDatabase() {
        String tagString = "tag1";
        when(tagParsingService.parseTagSet(tagString)).thenReturn(Set.of(EXISTING_TAG));
        when(tagRepository.findOne(Example.of(EXISTING_TAG))).thenReturn(Optional.of(EXISTING_TAG));

        final Photo result = photoSetUpService.setCorrectTags(SAMPLE_PHOTO_WITH_NO_TAGS, tagString);

        assertThat(result.getTags()).containsExactly(EXISTING_TAG);
        verify(tagParsingService).parseTagSet(tagString);
        verify(tagRepository, times(1)).findOne(Example.of(EXISTING_TAG));
        verifyNoInteractions(cloudinaryService);
    }

    @Test
    void shouldAddTagThatDoesNotExistInDatabase() {
        String tagString = "tag2";
        when(tagParsingService.parseTagSet(tagString)).thenReturn(Set.of(NEW_TAG));
        when(tagRepository.findOne(Example.of(NEW_TAG))).thenReturn(Optional.empty());

        final Photo result = photoSetUpService.setCorrectTags(SAMPLE_PHOTO_WITH_NO_TAGS, tagString);

        assertThat(result.getTags()).containsExactly(NEW_TAG);
        verify(tagParsingService).parseTagSet(tagString);
        verify(tagRepository, times(1)).findOne(Example.of(NEW_TAG));
        verifyNoInteractions(cloudinaryService);
    }

    @Test
    void shouldNotAddWhenEmptyStringOfTags() {
        String emptyStringOfTags = "";
        when(tagParsingService.parseTagSet(emptyStringOfTags)).thenReturn(Set.of());

        final Photo result = photoSetUpService.setCorrectTags(SAMPLE_PHOTO_WITH_NO_TAGS, emptyStringOfTags);

        assertThat(result.getTags()).isEmpty();
        verify(tagParsingService).parseTagSet(emptyStringOfTags);
        verifyNoInteractions(tagRepository, cloudinaryService);
    }

    @Test
    void shouldAddTwoTagsWhenOneIsInDatabaseAndOtherIsNot() {
        //given
        String tagString = "tag1 tag2";
        when(tagParsingService.parseTagSet(tagString)).thenReturn(Set.of(NEW_TAG, EXISTING_TAG));
        when(tagRepository.findOne(Example.of(EXISTING_TAG))).thenReturn(Optional.of(EXISTING_TAG));
        when(tagRepository.findOne(Example.of(NEW_TAG))).thenReturn(Optional.empty());

        //when
        final Photo result = photoSetUpService.setCorrectTags(SAMPLE_PHOTO_WITH_NO_TAGS, tagString);

        //then
        assertThat(result.getTags()).containsExactlyInAnyOrder(EXISTING_TAG, NEW_TAG);
        verify(tagParsingService, times(1)).parseTagSet(tagString);
        verify(tagRepository, times(1)).findOne(Example.of(EXISTING_TAG));
        verify(tagRepository, times(1)).findOne(Example.of(NEW_TAG));
        verifyNoInteractions(cloudinaryService);
    }

    @Test
    void shouldSetCorrectUrl() throws UploadFileFailureException {
        final MockMultipartFile multipartFile = new MockMultipartFile("url", "url".getBytes());
        when(cloudinaryService.upload(multipartFile)).thenReturn(Map.of("url", "url"));

        final Photo result = photoSetUpService.setCorrectUrl(SAMPLE_PHOTO_WITH_NO_TAGS, multipartFile);

        assertThat(result.getUrl()).isEqualTo("url");
        verify(cloudinaryService, times(1)).upload(multipartFile);
        verifyNoInteractions(tagParsingService, tagRepository);
    }

    @Test
    void shouldThrowWhenUploadingWringFile() throws UploadFileFailureException {
        final MockMultipartFile multipartFile = new MockMultipartFile("url", "url".getBytes());
        when(cloudinaryService.upload(multipartFile)).thenThrow(UploadFileFailureException.class);

        assertThatExceptionOfType(UploadFileFailureException.class)
                .isThrownBy(() -> photoSetUpService.setCorrectUrl(SAMPLE_PHOTO_WITH_NO_TAGS, multipartFile));
        verify(cloudinaryService, times(1)).upload(multipartFile);
        verifyNoInteractions(tagParsingService, tagRepository);
    }

}