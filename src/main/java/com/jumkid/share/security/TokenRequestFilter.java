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
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TokenRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.token.enable}")
    private boolean enableTokenCheck;

    @Value("${jwt.token.introspect.url}")
    private String tokenIntrospectUrl;

    private final RestTemplate restTemplate;

    private final TokenUser tokenUser;

    @Autowired
    public TokenRequestFilter(RestTemplate restTemplate, TokenUser tokenUser) {
        this.restTemplate = restTemplate;
        this.tokenUser = tokenUser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (enableTokenCheck) {
            try {
                if (tokenUser != null) {
                    String accessToken;

                        accessToken = tokenUser.getAuthorizationToken();

                        if (!isAccessTokenValid(accessToken)) {
                            log.warn("access token is invalid {}", accessToken);
                            handleInvalidAccessTokenResponse(response);
                        } else {
                            log.debug("token user roles: [{}]", String.join(",", tokenUser.getRoles()));
                            setContextAuthentication(tokenUser.getRoles());
                        }
                } else {
                    logger.warn("Authentication Token is not presented or does not begin with Bearer String");
                    throw new JwtTokenNotFoundException();
                }
            } catch (IllegalArgumentException iae) {
                log.error("Unable to get JWT Token");
            } catch (JwtExpiredException jee) {
                log.info("JWT Token has expired");
                //TODO: renew token if refresh presented

            }
        }

        filterChain.doFilter(request, response);
    }

    private void setContextAuthentication(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        UserDetails userDetails = User.builder()
                .username(tokenUser.getUsername())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .authorities(authorities)
                .disabled(false)
                .build();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private boolean isAccessTokenValid(String accessToken) {
        if (accessToken != null) {
            log.debug("verify access token {}", accessToken);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(accessToken);

            HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

            try {
                ResponseEntity<TokenSession> response = restTemplate.exchange(tokenIntrospectUrl, HttpMethod.GET, request,
                        TokenSession.class);
                TokenSession token = response.getBody();
                if (token != null) {
                    return token.getError() == null && token.isActive();
                }
            } catch (Exception e) {
                log.error("Failed to fetch token information from oauth provider {}", e.getMessage());
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
