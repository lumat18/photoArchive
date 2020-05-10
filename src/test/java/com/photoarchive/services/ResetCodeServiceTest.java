package com.photoarchive.services;

import com.photoarchive.domain.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Arrays;
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
        String testCreationDate = LocalDateTime.now().toString();

        final String resetCode = resetCodeService.createResetCode(token);
        final byte[] bytes = Base64.getDecoder().decode(resetCode);
        final String decoded = new String(bytes);
        final String decodedDateTime = decoded.replace("abc_", "");
        final LocalDateTime tokenCreationDate = LocalDateTime.parse(decodedDateTime);

        assertThat(decoded).contains(token.getValue());
        assertThat(tokenCreationDate).isAfterOrEqualTo(testCreationDate);
    }

    @Test
    void shouldExtractTokenCreationDateFromResetCode(){
        final LocalDateTime creationDate = LocalDateTime.now();
        String resetString = "abc_" + creationDate;
        String resetCode = Base64.getEncoder()
                .encodeToString((resetString).getBytes());

        final LocalDateTime extractedDate
                = resetCodeService.extractTokenCreationDate(resetCode);

        assertThat(extractedDate).isEqualTo(creationDate);
    }

    @Test
    void shouldThrowWhenCantParseDateFromResetCode(){
        String resetString = "abc";
        String resetCode = Base64.getEncoder()
                .encodeToString((resetString).getBytes());

        assertThatExceptionOfType(DateTimeException.class)
                .isThrownBy(()->resetCodeService.extractTokenCreationDate(resetCode));
    }
}