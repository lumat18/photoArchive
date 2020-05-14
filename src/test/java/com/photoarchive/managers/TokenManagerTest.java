package com.photoarchive.managers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TokenManagerTest {

    @Autowired
    private TokenManager tokenManager;

    final Integer tokenExpirationTimeInHours = TokenManager.TOKEN_EXPIRATION_TIME_IN_HOURS;

    //hasExpired method
    @Test
    void shouldReturnFalseIfCreationTimeWasLessThan24HoursAgo() {
        //given
        LocalDateTime creationTime = LocalDateTime.now().minusHours(tokenExpirationTimeInHours - 1);
        //when
        boolean result = tokenManager.hasExpired(creationTime);
        //then
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueIfCreationTimeWasMoreThan24HoursAgo() {
        //given
        LocalDateTime creationTime = LocalDateTime.now().minusHours(tokenExpirationTimeInHours + 1);
        //when
        boolean result = tokenManager.hasExpired(creationTime);
        //then
        assertTrue(result);
    }
}