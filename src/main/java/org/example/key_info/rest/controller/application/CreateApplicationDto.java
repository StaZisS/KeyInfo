package org.example.key_info.rest.controller.application;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateApplicationDto(
        @JsonProperty("application_creator_id")
        UUID applicationCreatorId,

        @JsonProperty("start_time")
        OffsetDateTime startTime,

        @JsonProperty("end_time")
        OffsetDateTime endTime
) {
}
