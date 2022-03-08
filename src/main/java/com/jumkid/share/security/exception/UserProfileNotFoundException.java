package com.jumkid.share.security.exception;

public class UserProfileNotFoundException extends RuntimeException{

    public static final String ERROR = "User profile is not found.";

    public UserProfileNotFoundException() { super(ERROR); }

    public UserProfileNotFoundException(String error) { super(error);}
}
