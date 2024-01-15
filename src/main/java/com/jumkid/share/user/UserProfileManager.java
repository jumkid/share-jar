package com.jumkid.share.user;

import com.jumkid.share.config.JwtTokenConfigProperties;
import com.jumkid.share.security.exception.UserProfileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
public class UserProfileManager {

    @Value("${internal.api.user}")
    private String userProfileAccessUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public UserProfileManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserProfile fetchUserProfile(String userId, String accessToken) {
        accessToken = Optional.ofNullable(accessToken).orElseGet(this::getAccessToken);

        log.debug("fetch user profile from {}", userProfileAccessUrl);
        String accessEndpoint = userProfileAccessUrl+"/"+userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (accessToken != null) headers.setBearerAuth(accessToken);

        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserProfile> response = restTemplate.exchange(accessEndpoint, HttpMethod.GET, request,
                    UserProfile.class);

            return response.getBody();
        } catch (Exception e) {
            log.error("failed to fetch user profile {}", e.getMessage());
        }

        return null;
    }

    public UserProfile fetchUserProfile() throws UserProfileNotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return UserProfile.builder()
                    .username(userDetails.getUsername()).id(userDetails.getPassword())
                    .build();
        } else {
            throw new UserProfileNotFoundException("User profile could not be found. Access is denied");
        }
    }

    public String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? (String)authentication.getCredentials() : null;
    }

}
