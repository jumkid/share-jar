package com.jumkid.share.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public class InternalRestApiException extends RuntimeException{

    @Getter
    private HttpStatusCode statusCode;

    private static final String ERROR = "Failed to call internal Rest API call";

    public InternalRestApiException(){ super(ERROR); }

    public InternalRestApiException(String message){ super(message); }

    public InternalRestApiException(HttpStatusCode statusCode, String message){
        super(message);
        this.statusCode = statusCode;
    }
}
