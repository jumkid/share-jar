package com.jumkid.share.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.jumkid.share.util.Constants.YYYYMMDDTHHMMSS3S;

@SuperBuilder @Data @AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(value = { "createdBy", "creationDate", "modifiedBy" })
public abstract class GenericDTO implements Serializable {

    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYYMMDDTHHMMSS3S)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdOn;

    private String modifiedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYYMMDDTHHMMSS3S)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedOn;
}
