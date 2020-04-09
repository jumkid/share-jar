package com.jumkid.share.config;

import com.jumkid.share.security.jwt.TokenUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class TokenSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TokenUser tokenUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        final String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getResponse();

            // set the token back to response
            assert response != null;
            response.setHeader(HEADER_AUTHORIZATION, authorizationHeader);

            String token = authorizationHeader.split(" ")[1];
            return new TokenUser(token);
        }

        return  null;
    }

}
