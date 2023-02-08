package com.jumkid.share.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumkid.share.controller.response.CommonResponse;
import com.jumkid.share.security.exception.AccessTokenInvaidException;
import com.jumkid.share.security.jwt.JwtToken;
import com.jumkid.share.security.jwt.JwtTokenParser;
import com.jumkid.share.security.jwt.TokenUser;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.jumkid.share.util.Constants.*;

@Slf4j
public class BearerTokenRequestFilter extends OncePerRequestFilter {

    private final boolean enableTokenCheck;

    private final String tokenIntrospectUrl;

    private final RestTemplate restTemplate;

    public BearerTokenRequestFilter(boolean enableTokenCheck,
                                    String tokenIntrospectUrl,
                                    RestTemplate restTemplate) {
        this.enableTokenCheck = enableTokenCheck;
        this.tokenIntrospectUrl = tokenIntrospectUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (enableTokenCheck) {
            try {
                TokenUser tokenUser = getTokenUser(request);
                String accessToken = tokenUser.getAuthorizationToken();

                if (!isAnonymousUser(tokenUser) && !isAccessTokenValid(accessToken)) {
                    log.warn("access token is invalid {}", accessToken);
                    handleInvalidAccessTokenResponse(response);
                } else {
                    setContextAuthentication(tokenUser);
                }
            } catch (IllegalArgumentException iae) {
                log.error("Unable to get access token");
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setContextAuthentication(TokenUser tokenUser) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (tokenUser.getRoles() != null) {
            StringBuilder sb = new StringBuilder();
            for (String role :tokenUser.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role));
                sb.append(role).append(" ");
            }
            log.debug("token user roles: [{}]", sb.toString());
        }

        UserDetails userDetails = User.builder()
                .username(tokenUser.getUsername())
                .password(tokenUser.getUserId())  //** set userId as password as useId is used internally only
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .authorities(authorities)
                .disabled(false)
                .build();

        //* set token string as credential for context use *//
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, tokenUser.getAuthorizationToken(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private boolean isAnonymousUser(TokenUser tokenUser) {
        return tokenUser.getUsername().equals(ANONYMOUS_USER);
    }

    private boolean isAccessTokenValid(String accessToken) {
        if (accessToken != null) {
            log.debug("verify access token {}", accessToken);
            HttpHeaders headers = new HttpHeaders();
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

    private TokenUser getTokenUser(HttpServletRequest request) {
        final String HEADER_AUTHORIZATION = "Authorization";
        final String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getResponse();

            // set the token back to response
            assert response != null;
            response.setHeader(HEADER_AUTHORIZATION, authorizationHeader);

            String token = authorizationHeader.split("\\s+")[1];
            Optional<JwtToken> optional = JwtTokenParser.parse(token);
            if (optional.isPresent()) {
                JwtToken jwtToken = optional.get();

                log.debug("Authentication bearer token is processed successfully for user {}", jwtToken.getUsername());

                return TokenUser.builder()
                        .authorizationToken(token)
                        .userId(jwtToken.getUserId())
                        .username(jwtToken.getUsername())
                        .displayName(jwtToken.getDisplayName())
                        .givenName(jwtToken.getGivenName())
                        .familyName(jwtToken.getFamilyName())
                        .email(jwtToken.getEmailAddress())
                        .companyIds(jwtToken.getCompanyIds())
                        .roles(jwtToken.getRealmAccess().getOrDefault("roles",
                                Collections.unmodifiableList(new ArrayList<>())))
                        .build();
            }
        }

        log.debug("Authentication Token is not presented or does not begin with Bearer String");

        return TokenUser.builder()
                .userId(ANONYMOUS_USER).username(ANONYMOUS_USER).displayName(ANONYMOUS_USER_DISPLAY)
                .roles(List.of(GUEST_ROLE))
                .build();
    }

}
