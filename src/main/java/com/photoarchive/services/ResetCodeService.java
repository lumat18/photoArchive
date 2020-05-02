package com.photoarchive.services;

import com.photoarchive.domain.Token;
import com.photoarchive.exceptions.EmailNotFoundException;
import com.photoarchive.managers.TokenManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class ResetCodeService {

    private TokenManager tokenManager;

    @Autowired
    public ResetCodeService(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public String createResetCode(String email) throws EmailNotFoundException {
        Token token = tokenManager
                .findTokenByUsersEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        return Base64.getEncoder()
                .encodeToString((token.getValue() + "_" + getExpirationDate()).getBytes());
    }

    private String getExpirationDate() {
        return LocalDateTime.now().plusHours(24).toString();
    }

    public String extractTokenValue(String resetCode){
        return StringUtils.substringBefore(decode(resetCode), "_");
    }

    public LocalDateTime extractExpirationDate(String resetCode) throws DateTimeException {
        return LocalDateTime.parse(StringUtils.substringAfter(decode(resetCode), "_"));
    }

    private String decode(String resetCode){
        byte[] decodedBytes = Base64.getDecoder().decode(resetCode);
        return new String(decodedBytes);
    }
}
