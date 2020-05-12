package com.photoarchive.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {

    private EmailValidator emailValidator = new EmailValidator();

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Test
    void shouldBeValid() {
        //given
        String validEmail = "abc@abc.pl";
        //when
        boolean result = emailValidator.isValid(validEmail, constraintValidatorContext);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldBeValidWithUnderScore() {
        //given
        String validEmail = "abc_123@abc.pl";
        //when
        boolean result = emailValidator.isValid(validEmail, constraintValidatorContext);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotBeValidWhenThereIsNoAt() {
        //given
        String invalidEmail = "abcabc.pl";
        //when
        boolean result = emailValidator.isValid(invalidEmail, constraintValidatorContext);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldNotBeValidWhenThereIsNoDot() {
        //given
        String invalidEmail = "abc@abcpl";
        //when
        boolean result = emailValidator.isValid(invalidEmail, constraintValidatorContext);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldNotBeValidWhenThereAreSpecialSymbolsBeforeAt() {
        //given
        String invalidEmail = "ab%^c@abc.pl";
        //when
        boolean result = emailValidator.isValid(invalidEmail, constraintValidatorContext);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldNotBeValidWhenThereAreSpecialSymbolsAfterAt() {
        //given
        String invalidEmail = "abc@a&$)bc.pl";
        //when
        boolean result = emailValidator.isValid(invalidEmail, constraintValidatorContext);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldNotBeValidWhenEndsWithDot() {
        //given
        String invalidEmail = "abc@abc.";
        //when
        boolean result = emailValidator.isValid(invalidEmail, constraintValidatorContext);
        //then
        assertThat(result).isFalse();
    }

}