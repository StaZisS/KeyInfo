package org.example.key_info.rest.controller.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record FreeAudienceResponseDto(
        @JsonProperty("build_id")
        int buildId,

        @JsonProperty("room_id")
        int roomId,

        @JsonProperty("start_time")
        OffsetDateTime startTime,

        @JsonProperty("end_time")
        OffsetDateTime endTime
) {
}
