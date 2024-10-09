package com.jumkid.share.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class InternalRestApiException extends Exception{

    private final HttpStatusCode statusCode;

    private static final String ERROR = "Failed to call internal Rest API call";

    public InternalRestApiException(){
        this(ERROR);
    }

    public InternalRestApiException(String message){
        super(message);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public InternalRestApiException(HttpStatusCode statusCode, String message){
        super(message);
        this.statusCode = statusCode;
    }
}
