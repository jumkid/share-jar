package com.jumkid.share.security.exception;

public class JwtTokenNotFoundException extends RuntimeException{

    private static final String ERROR = "Jwt token is not found in the header. Please provide a valid Jwt token to authenticate the request.";

    public JwtTokenNotFoundException(){ super(ERROR); }

}
