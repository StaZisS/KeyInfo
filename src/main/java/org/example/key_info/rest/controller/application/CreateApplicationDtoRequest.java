package org.example.key_info.rest.controller.application;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record CreateApplicationDtoRequest(
        @JsonProperty(value = "start_time", required = true)
        OffsetDateTime startTime,

        @JsonProperty(value = "end_time", required = true)
        OffsetDateTime endTime,

        @JsonProperty(value = "build_id", required = true)
        int buildId,

        @JsonProperty(value = "room_id", required = true)
        int roomId,

        @JsonProperty(value = "under_which_role_perform", required = true)
        String role,

        @JsonProperty(value = "is_duplicate", defaultValue = "false")
        boolean isDuplicate,

        @JsonProperty(value = "until_when_duplicate")
        OffsetDateTime endTimeToDuplicate
) {
}
