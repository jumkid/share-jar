package com.jumkid.share.controller.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CommonResponse {

    public enum ErrorCodes {
        ERROR_DB("error_db"), ERROR_VALIDATION("error_validation");

        private String code;

        private ErrorCodes(String code){ this.code = code; }

        public String code(){ return code; }
    }

    //indicate response successful or failed
    private boolean success;
    //error code for error reference
    private String errorCode;
    //some information for success or error
    private String msg;
    //the data json object
    private Object data;

}
