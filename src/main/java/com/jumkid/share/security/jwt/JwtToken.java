package com.jumkid.share.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;

@Slf4j
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtToken implements Serializable {

    private JwtTokenData tokenData;

    public JwtToken(String token) {
        String[] splitString = token.split("\\.");

        if (splitString.length != 3) {
            log.error("JWT token could not be split into three parts ({}).", token);
        } else {
            String base64EncodedBody = splitString[1];

            Base64.Decoder decoder = Base64.getUrlDecoder();
            String tokenBody = new String(decoder.decode(base64EncodedBody));

            ObjectMapper mapper = new ObjectMapper();
            try {
                tokenData = mapper.readValue(tokenBody, JwtTokenData.class);
            } catch (IOException e) {
                log.error("Could not read data from JWT token ({}).", token, e);
            }
        }
    }
}