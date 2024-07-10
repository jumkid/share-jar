package com.jumkid.share.config;

import com.jumkid.share.security.BearerTokenRequestFilter;
import com.jumkid.share.user.UserProfileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenConfigProperties jwtTokenConfigProperties;

    private final RestTemplate restTemplate;

    public SecurityConfig(JwtTokenConfigProperties jwtTokenConfigProperties, RestTemplate restTemplate) {
        this.jwtTokenConfigProperties = jwtTokenConfigProperties;
        this.restTemplate = restTemplate;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(new BearerTokenRequestFilter(jwtTokenConfigProperties.isEnable(),
                                jwtTokenConfigProperties.isValidate(), jwtTokenConfigProperties.getIntrospectUrl(),
                                restTemplate),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public UserProfileManager userProfileManager(RestTemplate restTemplate) {
        return new UserProfileManager(restTemplate);
    }

}
