package com.photoarchive.services;

import com.photoarchive.domain.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class ResetCodeServiceTest {

    @Autowired
    private ResetCodeService resetCodeService;

    @Test
    void shouldCreateResetCodeFromToken() {
        final Token token = new Token();
        token.setValue("abc");
        String creationDate = LocalDateTime.now().toString();
        String code = Base64.getEncoder()
                .encodeToString(("abc_" + creationDate).getBytes());

        final String resetCode = resetCodeService.createResetCode(token);

        assertThat(resetCode).isEqualTo(code);
    }

    @Test
    void shouldExtractTokenCreationDateFromResetCode() {
        final LocalDateTime creationDate = LocalDateTime.now();
        String resetString = "abc_" + creationDate;
        String resetCode = Base64.getEncoder()
                .encodeToString((resetString).getBytes());

        final LocalDateTime extractedDate
                = resetCodeService.extractTokenCreationDate(resetCode);

        assertThat(extractedDate).isEqualTo(creationDate);
    }

    @Test
    void shouldThrowWhenCantParseDateFromResetCode() {
        String resetString = "abc";
        String resetCode = Base64.getEncoder()
                .encodeToString((resetString).getBytes());

        assertThatExceptionOfType(DateTimeException.class)
                .isThrownBy(() -> resetCodeService.extractTokenCreationDate(resetCode));
    }

    @Test
    void shouldExtractTokenValueFromResetCode(){
        final String resetCode = "tokenValue_DateOfCreation";
        final String encodedResetCode = Base64.getEncoder().encodeToString(resetCode.getBytes());

        final String extractedTokenValue = resetCodeService.extractTokenValue(encodedResetCode);

        assertThat(extractedTokenValue).isEqualTo("tokenValue");
    }
}