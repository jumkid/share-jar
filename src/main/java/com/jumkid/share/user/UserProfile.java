package com.jumkid.share.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class UserProfile {

    private String id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private boolean enabled;

}
