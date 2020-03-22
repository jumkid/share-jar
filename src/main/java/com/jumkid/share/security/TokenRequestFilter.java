package com.jumkid.share.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumkid.share.controller.response.CommonResponse;
import com.jumkid.share.security.exception.AccessTokenInvaidException;
import com.jumkid.share.security.exception.JwtExpiredException;
import com.jumkid.share.security.exception.JwtTokenNotFoundException;
import com.jumkid.share.security.jwt.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class TokenRequestFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    @Value("${oauth.provider.token.enable}")
    private boolean enableTokenCheck;

    @Value("${oauth.provider.token.introspect.url}")
    private String tokenIntrospectUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public TokenRequestFilter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (enableTokenCheck) {
            final String requestTokenHeader = request.getHeader(HEADER_AUTHORIZATION);

            String username = null;
            String accessToken = null;

            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                String token = requestTokenHeader.substring(7);
                try {
                    TokenUser tokenUser = new TokenUser(token);
                    username = tokenUser.getUsername();
                    if (username != null) {
                        log.info("Found jwt token with username: {}", username);
                        accessToken = tokenUser.getAuthorizationToken();
                    } else {
                        accessToken = token;
                    }

                    // set the token back to response
                    response.setHeader(HEADER_AUTHORIZATION, requestTokenHeader);

                    if (!isAccessTokenValid(accessToken)) {
                        log.info("access token is invalid {}", accessToken);
                        handleInvalidAccessTokenResponse(response);
                        return;
                    }

                } catch (IllegalArgumentException iae) {
                    log.error("Unable to get JWT Token");
                } catch (JwtExpiredException jee) {
                    log.info("JWT Token has expired");
                    //TODO: renew token if refresh presented

                }
            } else {
                logger.warn("JWT Token is not presented or does not begin with Bearer String");
                throw new JwtTokenNotFoundException();
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAccessTokenValid(String accessToken) {
        if (accessToken != null) {
            log.debug("verify access token {}", accessToken);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("token", accessToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            try {
                TokenSession session = restTemplate.postForObject(tokenIntrospectUrl, request, TokenSession.class);
                if (session != null) {
                    //TODO: renew token if refresh presented and it will expire in timeout threshold
                    return session.isActive();
                }
            } catch (Exception e) {
                log.error("Failed to fetch token information from oauth provider");
            }
        }

        return false;
    }

    private void handleInvalidAccessTokenResponse(HttpServletResponse response) {
        int statusCode = HttpStatus.FORBIDDEN.value();
        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        ObjectMapper mapper = new ObjectMapper();
        try (PrintWriter out = response.getWriter()) {
            out.print(mapper.writeValueAsString(CommonResponse.builder()
                    .success(false).errorCode(String.valueOf(statusCode)).msg(AccessTokenInvaidException.ERROR)
                    .build())
            );
            out.flush();
        } catch (Exception e) {
            throw new AccessTokenInvaidException();
        }
    }

}
