package com.photoarchive.managers;

import com.photoarchive.repositories.TokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenManagerTest {

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private TokenManager tokenManager;

    //hasExpired method
    @Test
    void shouldReturnFalseIfCreationTimeWasLessThan24HoursAgo(){
        //given
        LocalDateTime creationTime = LocalDateTime.now().minusHours(20);
        //when
        boolean result = tokenManager.hasExpired(creationTime);
        //then
        assertFalse(result);
    }
    @Test
    void shouldReturnTrueIfCreationTimeWasMoreThan24HoursAgo(){
        //given
        LocalDateTime creationTime = LocalDateTime.now().minusHours(25);
        //when
        boolean result = tokenManager.hasExpired(creationTime);
        //then
        assertTrue(result);
    }
    

}