package com.jumkid.share.security.exception;

public class AccessTokenInvaidException extends RuntimeException {

    public static final String ERROR = "Access token is invalid. Please provide a valid access token.";

    public AccessTokenInvaidException() { super(ERROR); }

}
