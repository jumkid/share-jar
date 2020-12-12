package com.jumkid.share.user;

import lombok.Data;

import java.util.List;

@Data
public class UserProfile {

    private String id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    //private List<String> attributes;

    private boolean enabled;

}
