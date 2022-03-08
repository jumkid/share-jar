package com.jumkid.share.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserProfile {

    private String id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    //private List<String> attributes;

    private boolean enabled;

}
