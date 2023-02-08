package com.jumkid.share.security.exception;

public class InternalRestApiException extends RuntimeException{

    private static final String ERROR = "Failed to call internal Rest API call";

    public InternalRestApiException(){ super(ERROR); }

    public InternalRestApiException(String message){ super(message); }
}
