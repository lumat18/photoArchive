package com.photoarchive.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StrongPasswordValidatorTest {

    private StrongPasswordValidator strongPasswordValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp(){
        strongPasswordValidator = new StrongPasswordValidator();
    }

    @Test
    void shouldBeValidPassword(){
        //given
        final String validPassword = "abcd1";
        //when
        boolean result = strongPasswordValidator
                .isValid(validPassword, constraintValidatorContext);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldBeTooShortPassword(){
        //given
        final String tooShortPassword = "abc";
        //when
        boolean result = strongPasswordValidator
                .isValid(tooShortPassword, constraintValidatorContext);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldBeInvalidPasswordWithNoDigits(){
        //given
        final String passwordWithoutDigits = "abcde";
        //when
        boolean result = strongPasswordValidator
                .isValid(passwordWithoutDigits, constraintValidatorContext);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldBeInvalidPasswordWithoutLetters(){
        //given
        final String passwordWithoutLetters = "12345";
        //when
        boolean result = strongPasswordValidator
                .isValid(passwordWithoutLetters, constraintValidatorContext);
        //then
        assertThat(result).isFalse();
    }
}