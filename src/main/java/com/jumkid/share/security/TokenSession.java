package com.jumkid.share.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenSession {

        private boolean active;

        @JsonProperty("client_id")
        private String clientId;

        @JsonProperty("sub")
        private String subject;

        @JsonProperty("exp")
        private Integer expireAt;

        @JsonProperty("iat")
        private Integer issuedAt;

        @JsonProperty("iss")
        private String issuedBy;

        @JsonProperty("token_type")
        private String tokenType;

        private String username;

        private String scope;

}
