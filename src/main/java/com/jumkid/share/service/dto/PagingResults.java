package com.jumkid.share.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagingResults<T> {

    // total number of records
    private Long total;
    // size of page
    private Integer size;
    // page number
    private Integer page;
    //the list of result set
    private List<T> resultSet;

}
