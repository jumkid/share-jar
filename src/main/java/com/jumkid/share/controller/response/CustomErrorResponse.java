package com.jumkid.share.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class CustomErrorResponse {

    private Date timestamp;

    private String message;

    private List<String> property;

    private List<String> details;

    private String httpCodeMessage;

    public CustomErrorResponse(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

}
