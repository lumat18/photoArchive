package com.photoarchive.services;

import com.photoarchive.domain.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class ResetCodeService {

    public String createResetCode(Token token) {

        return Base64.getEncoder()
                .encodeToString((token.getValue() + "_" + getTokenCreationDate()).getBytes());
    }

    private String getTokenCreationDate() {
        return LocalDateTime.now().toString();
    }

    public String extractTokenValue(String resetCode){
        return StringUtils.substringBefore(decode(resetCode), "_");
    }

    public LocalDateTime extractTokenCreationDate(String resetCode) throws DateTimeException {
        return LocalDateTime.parse(StringUtils.substringAfter(decode(resetCode), "_"));
    }

    private String decode(String resetCode){
        byte[] decodedBytes = Base64.getDecoder().decode(resetCode);
        return new String(decodedBytes);
    }
}
