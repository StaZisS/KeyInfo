package org.example.key_info.rest.controller.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record DayTimeSlots(
        @JsonProperty("time_slots")
        List<TimeSlot> timeSlots,

        @JsonProperty("time")
        OffsetDateTime time
) {
}
