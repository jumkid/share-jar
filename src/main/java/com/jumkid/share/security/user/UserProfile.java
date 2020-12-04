package com.jumkid.share.security.user;

import lombok.Data;

@Data
public class UserProfile {

    private String id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private boolean enabled;

}
