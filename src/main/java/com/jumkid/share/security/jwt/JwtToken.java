package com.jumkid.share.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtToken implements Serializable {

    @JsonProperty("sub")
    private String userId;

    @JsonProperty("name")
    private String displayName;

    @JsonProperty("preferred_username")
    private String username;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("email")
    private String emailAddress;

    @JsonProperty("companyIds")
    private List<Integer> companyIds;

    @JsonProperty("email_verified")
    private boolean emailVerified;

    @JsonProperty("realm_access")
    private Map<String, List<String>> realmAccess = new HashMap<>();

    @JsonProperty("resource_access")
    private Map<String, Map<String, List<String>>> resourceAccess;

}

