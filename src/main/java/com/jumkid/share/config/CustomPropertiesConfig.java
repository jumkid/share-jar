package com.jumkid.share.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtTokenConfigProperties.class)
public class CustomPropertiesConfig {

}
