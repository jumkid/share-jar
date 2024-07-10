package com.jumkid.share.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties(prefix = "com.jumkid.jwt.token")
public class JwtTokenConfigProperties {
    boolean enable;

    boolean validate;

    String clientId;

    String clientSecret;

    String fetchUrl;

    String introspectUrl;
}
