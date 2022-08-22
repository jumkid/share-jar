package com.jumkid.share.controller.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagingResponse<T> {

    //indicate response successful or failed
    private boolean success;
    //some information for success or error
    private String msg;
    // total number of records
    private Long total;
    // size of page
    private Integer size;
    // page number
    private Integer page;
    //the list of json objects
    private List<T> data;

}
