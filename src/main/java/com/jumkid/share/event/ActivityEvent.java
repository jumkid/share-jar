package com.jumkid.share.event;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityEvent extends CommonEvent{

    private Long activityId;

    @Builder
    public ActivityEvent(Long activityId,
                         String journeyId,
                         String topic,
                         String sentBy,
                         LocalDateTime creationDate,
                         String payload) {
        super(journeyId, topic, sentBy, creationDate, payload);
        this.activityId = activityId;
    }

}
