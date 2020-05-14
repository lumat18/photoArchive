package com.photoarchive.managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class ResetCodeManager {

    private Base64.Encoder encoder;
    private Base64.Decoder decoder;

    @Autowired
    public ResetCodeManager(Base64.Encoder encoder, Base64.Decoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public String createResetCode(String tokenValue) {
        return encoder.encodeToString((tokenValue + "_" + getCreationDate()).getBytes());
    }

    private String getCreationDate() {
        return LocalDateTime.now().toString();
    }

    public String extractTokenValue(String resetCode){
        return StringUtils.substringBefore(decode(resetCode), "_");
    }

    public LocalDateTime extractCreationDate(String resetCode) throws DateTimeException {
        return LocalDateTime.parse(StringUtils.substringAfter(decode(resetCode), "_"));
    }

    private String decode(String resetCode){
        byte[] decodedBytes = decoder.decode(resetCode);
        return new String(decodedBytes);
    }
}
