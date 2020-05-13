package com.photoarchive.managers;

import com.photoarchive.domain.Token;
import com.photoarchive.domain.User;
import com.photoarchive.repositories.TokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class TokenManagerTest {

//    @MockBean
//    private TokenRepository tokenRepository;

    @Autowired
    private TokenManager tokenManager;

//    @Captor
//    private ArgumentCaptor<Token> captor;

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

//    @Test
//    void shouldCreateTokenForGivenUser(){
//        //given
//        final User user = new User();
//        when(tokenRepository.save(user.getToken())).thenReturn(null);
//        //when
//        tokenManager.createToken(user);
//        //then
//        verify(tokenRepository).save(captor.capture());
//        assertThat(captor.getValue().getUser()).isEqualTo(user);
//    }
}