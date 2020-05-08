package com.photoarchive.services;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.Tag;
import com.photoarchive.repositories.PhotoRepository;
import com.photoarchive.repositories.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Example;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SearchServiceTest {

    private final String ONE_EXISTING_TAG = "one";
    private final String ONE_NON_EXISTING_TAG = "one";
    private final String TWO_EXISTING_TAGS = "one two";
    private final String ONE_EXISTING_ONE_NOT_TAGS = "one two";
    private Tag tag1;
    private Tag tag2;
    private Set<Tag> querySet;
    private Photo photo1;
    private Photo photo2;



    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagParsingService tagParsingService;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    private void setUp(){
        tag1 = new Tag();
        tag2 = new Tag();
        photo1 = new Photo();
        photo1.setPhoto_id(1L);
        photo2 = new Photo();
        photo2.setPhoto_id(2L);
        querySet = new HashSet<>();
    }

    @Test
    public void shouldGetPhotosByOneExistingTag(){
        //given
        tag1.setName(ONE_EXISTING_TAG);
        querySet.add(tag1);
        photo1.setTags(querySet);

        when(tagParsingService.parseTagSet(ONE_EXISTING_TAG)).thenReturn(querySet);
        when(tagRepository.findOne(Example.of(tag1))).thenReturn(Optional.of(tag1));
        when(photoRepository.findDistinctByTagsIn(querySet,(long)querySet.size())).thenReturn(Set.of(photo1));
        //when
        Set<Photo> result = searchService.getPhotosByTags(ONE_EXISTING_TAG);
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.stream().map(Photo::getTags)).size().isEqualTo(1);
        assertThat(result.stream().map(Photo::getTags)).isSubsetOf(querySet);
        verify(tagParsingService, times(1)).parseTagSet(ONE_EXISTING_TAG);
        verify(tagRepository, times(1)).findOne(Example.of(tag1));
        verify(photoRepository, times(1)).findDistinctByTagsIn(querySet, (long)querySet.size());
    }

    @Test
    public void shouldNotFindIfNoExistingTagsGiven(){
        tag1.setName(ONE_NON_EXISTING_TAG);

        when(tagParsingService.parseTagSet(ONE_NON_EXISTING_TAG)).thenReturn(Set.of());
        when(photoRepository.findDistinctByTagsIn(querySet,(long)querySet.size())).thenReturn(Set.of());

        //when
        Set<Photo> result = searchService.getPhotosByTags(ONE_NON_EXISTING_TAG);
        //then
        assertThat(result.size()).isEqualTo(0);
        verify(tagParsingService, times(1)).parseTagSet(ONE_NON_EXISTING_TAG);
        verifyNoInteractions(tagRepository);
        verify(photoRepository, times(1)).findDistinctByTagsIn(querySet, (long)querySet.size());
    }

    @Test
    public void shouldFindTwoPhotosWithSameTag(){
        //given
        tag1.setName("one");
        tag2.setName("two");
        querySet.add(tag1);
        querySet.add(tag2);
        photo1.setTags(querySet);
        photo2.setTags(querySet);

        when(tagParsingService.parseTagSet(TWO_EXISTING_TAGS)).thenReturn(querySet);
        when(tagRepository.findOne(Example.of(tag1))).thenReturn(Optional.of(tag1));
        when(tagRepository.findOne(Example.of(tag2))).thenReturn(Optional.of(tag2));
        doReturn(Set.of(photo1,photo2)).when(photoRepository).findDistinctByTagsIn(querySet,(long)querySet.size());
        //when
        Set<Photo> result = searchService.getPhotosByTags(TWO_EXISTING_TAGS);
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.stream().map(Photo::getTags).collect(Collectors.toList()).get(0).size()).isEqualTo(2);
        assertThat(result.stream().map(Photo::getTags)).isSubsetOf(querySet);

        verify(tagParsingService, times(1)).parseTagSet(TWO_EXISTING_TAGS);
        verify(tagRepository, times(1)).findOne(Example.of(tag1));
        verify(tagRepository, times(1)).findOne(Example.of(tag2));
        verify(photoRepository, times(1)).findDistinctByTagsIn(querySet, (long)querySet.size());
    }

    @Test
    public void shouldFindOnlyOnePhotoIfOnlyOneHasBothQueryTags(){
        //given
        tag1.setName("one");
        tag2.setName("two");

        querySet.add(tag1);
        querySet.add(tag2);
        photo1.setTags(querySet);
        photo2.setTags(Set.of(tag1));

        when(tagParsingService.parseTagSet(ONE_EXISTING_ONE_NOT_TAGS)).thenReturn(querySet);
        when(tagRepository.findOne(Example.of(tag1))).thenReturn(Optional.of(tag1));
        when(tagRepository.findOne(Example.of(tag2))).thenReturn(Optional.of(tag2));
        doReturn(Set.of(photo1)).when(photoRepository).findDistinctByTagsIn(querySet,(long)querySet.size());
        //when
        Set<Photo> result = searchService.getPhotosByTags(ONE_EXISTING_ONE_NOT_TAGS);
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.stream().map(Photo::getTags).collect(Collectors.toList()).get(0).size()).isEqualTo(2);
        assertThat(result.stream().map(Photo::getTags)).isSubsetOf(querySet);

        verify(tagParsingService, times(1)).parseTagSet(ONE_EXISTING_ONE_NOT_TAGS);
        verify(tagRepository, times(1)).findOne(Example.of(tag1));
        verify(tagRepository, times(1)).findOne(Example.of(tag2));
        verify(photoRepository, times(1)).findDistinctByTagsIn(querySet, (long)querySet.size());

    }
}