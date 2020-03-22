package com.jumkid.share.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorResponse {
    private Date timestamp;

    private String message;

    private List<String> property;

    private List<String> details;

    private String httpCodeMessage;

    public CustomErrorResponse(Date timestamp, String message) {
        this(timestamp, message, null, null, null);
    }

    private CustomErrorResponse(Date timestamp, String message, List<String> property,
                               List<String> details, String httpCodeMessage) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.property = property;
        this.details = details;
        this.httpCodeMessage = httpCodeMessage;
    }
}
