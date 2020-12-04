package com.jumkid.share.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
public class JwtTokenParser {

    private JwtTokenParser() {}

    public static Optional<JwtToken> parse(String token) {
        String[] splitString = token.split("\\.");

        if (splitString.length != 3) {
            log.error("JWT token could not be split into three parts ({}).", token);
        } else {
            String base64EncodedBody = splitString[1];

            Base64.Decoder decoder = Base64.getUrlDecoder();
            String tokenBody = new String(decoder.decode(base64EncodedBody));

            ObjectMapper mapper = new ObjectMapper();
            try {
                JwtToken tokenData = mapper.readValue(tokenBody, JwtToken.class);
                return Optional.of(tokenData);
            } catch (IOException e) {
                log.error("Could not read data from JWT token ({}).", token, e);
            }
        }
        return Optional.empty();
    }
}
