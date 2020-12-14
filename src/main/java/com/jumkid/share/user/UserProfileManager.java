package com.jumkid.share.user;

import com.jumkid.share.security.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class UserProfileManager {

    @Value("${user.profile.access.url}")
    private String userProfileAccessUrl;

    @Value("${jwt.token.fetch.url}")
    private String accessTokenFetchUrl;

    @Value("${jwt.token.client.id}")
    private String clientId;

    @Value("${jwt.token.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    private AccessToken accessToken = null;

    @Autowired
    public UserProfileManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserProfile fetchUserProfile(String userId, String token) {
        if (token == null) {
            if (accessToken == null) accessToken = fetchAccessToken();

            if (accessToken != null) token = accessToken.getToken();
            else return null;
        }

        log.debug("fetch user profile from {}", userProfileAccessUrl);
        String accessEndpoint = userProfileAccessUrl+"/"+userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);

        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserProfile> response = restTemplate.exchange(accessEndpoint, HttpMethod.GET, request,
                    UserProfile.class);
            if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
                accessToken = null;
                fetchUserProfile(userId, null);
            }

            return response.getBody();
        } catch (Exception e) {
            log.error("failed to fetch user profile {}", e.getMessage());
        }

        return null;
    }

    private AccessToken fetchAccessToken() {
        log.debug("fetch service access token from {}", accessTokenFetchUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<AccessToken> response = restTemplate.postForEntity(accessTokenFetchUrl, request, AccessToken.class);

            return response.getBody();
        } catch (Exception e) {
            log.error("failed to fetch access token {}", e.getMessage());
        }

        return null;
    }

}
