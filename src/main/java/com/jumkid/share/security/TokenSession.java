package com.jumkid.share.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenSession {

        private boolean active = true;

        @JsonProperty("sub")
        private String subject;

        private String email;

        private String name;

        @JsonProperty("preferred_username")
        private String preferredUsername;

        @JsonProperty("given_name")
        private String givenName;

        @JsonProperty("family_name")
        private String familyName;

        private String error;

        @JsonProperty("error_description")
        private String errorDescription;

}
