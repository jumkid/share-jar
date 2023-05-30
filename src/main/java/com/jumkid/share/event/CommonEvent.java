package com.jumkid.share.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommonEvent {

    private String journeyId;

    private String topic;

    private String sentBy;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    )
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creationDate;

    private String payload;

    public CommonEvent(String journeyId, String topic, String sentBy, LocalDateTime creationDate, String payload) {
        this.journeyId = journeyId;
        this.topic = topic;
        this.sentBy = sentBy;
        this.creationDate = creationDate;
        this.payload = payload;
    }
}
