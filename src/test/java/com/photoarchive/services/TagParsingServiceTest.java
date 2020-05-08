package com.photoarchive.services;

import com.photoarchive.domain.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static com.photoarchive.services.TagParsingService.SEPARATOR;
import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
class TagParsingServiceTest {

    private final String WRONG_SEPARATOR = Character.toString(SEPARATOR.charAt(0)+1);

    private final String SINGLE_WORD_TAG = "one";
    private final String TWO_TAGS_SEPARATED_BY_SEPARATOR = "one"+ SEPARATOR+"two";
    private final String TWO_TAGS_SEPARATED_BY_THREE_SEPARATORS = "one"+ SEPARATOR+ SEPARATOR+ SEPARATOR+"two";
    private final String THREE_SEPARATORS = SEPARATOR+SEPARATOR+SEPARATOR;
    private final String TAG_PRECEEDED_BY_TWO_SEPARATORS = SEPARATOR+SEPARATOR+"one";
    private final String TWO_TAGS_SEPARATED_BY_WRONG_SEPARATOR = "one"+WRONG_SEPARATOR+"two";

    @Autowired
    private TagParsingService tagParsingService;

    @Test
    public void shouldReturnEmptySetIfOnlySeparatorsGiven(){
        //given
        //when
        Set<Tag> result = tagParsingService.parseTagSet(THREE_SEPARATORS);
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnOneTagIfSingleWordGiven(){
        //given
        //when
        Set<Tag> result = tagParsingService.parseTagSet(SINGLE_WORD_TAG);
        //then

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.contains(SINGLE_WORD_TAG));
    }

    @Test
    public void shouldReturnTwoTagsIfSeparatedBySeparator(){
        //given
        //when
        Set<Tag> result = tagParsingService.parseTagSet(TWO_TAGS_SEPARATED_BY_SEPARATOR);
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.stream().map(Tag::getName)).containsExactlyInAnyOrder("one", "two");
    }

    @Test
    public void shouldReturnTwoTagsIfSeparatedByMoreThanOneSeparator(){
        //given
        //when
        Set<Tag> result = tagParsingService.parseTagSet(TWO_TAGS_SEPARATED_BY_THREE_SEPARATORS);
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.stream().map(Tag::getName)).containsExactlyInAnyOrder("one", "two");
    }

    @Test
    public void shouldReturnOneTagIfItIsPreceededBySeparators(){
        //given
        //when
        Set<Tag> result = tagParsingService.parseTagSet(TAG_PRECEEDED_BY_TWO_SEPARATORS);
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.stream().map(Tag::getName)).containsExactly("one");
    }

    @Test
    public void shouldFailToParseIfSeparatorIsWrong(){
        //given
        //when
        Set<Tag> result = tagParsingService.parseTagSet(TWO_TAGS_SEPARATED_BY_WRONG_SEPARATOR);
        //then
        assertThat(result.size()).isNotEqualTo(2);
        assertThat(result.stream().map(Tag::getName)).doesNotContain("one", "two");
    }
}