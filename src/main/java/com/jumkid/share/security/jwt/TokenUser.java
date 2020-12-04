package com.jumkid.share.security.jwt;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Builder
@Getter
public class TokenUser {

    private String authorizationToken;

    private String userId;

    private String username;

    private String displayName;

    private String givenName;

    private String familyName;

    private String email;

    private List<Integer> companyIds;

    private List<String> roles;

}