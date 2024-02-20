package org.example.key_info.rest.controller.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record DayTimeSlots(
        @JsonProperty("time_slots")
        List<TimeSlot> timeSlots,

        @JsonProperty("time")
        OffsetDateTime time
) {
}
