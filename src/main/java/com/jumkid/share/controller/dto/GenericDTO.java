package com.jumkid.share.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.jumkid.share.util.Constants.YYYYMMDDTHHMMSS3S;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@JsonIgnoreProperties(value = { "createdBy", "creationDate", "modifiedBy" })
public abstract class GenericDTO {

    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYYMMDDTHHMMSS3S)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creationDate;

    private String modifiedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYYMMDDTHHMMSS3S)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modificationDate;

}
