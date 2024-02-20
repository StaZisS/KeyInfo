package org.example.key_info.rest.controller.application;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ApplicationResponseDto(
        @JsonProperty("application_id")
        UUID applicationId,

        @JsonProperty("owner_id")
        UUID ownerId,

        @JsonProperty("owner_name")
        String ownerName,

        @JsonProperty("owner_email")
        String ownerEmail,

        @JsonProperty("start_time")
        OffsetDateTime startTime,

        @JsonProperty("end_time")
        OffsetDateTime endTime,

        String status,

        @JsonProperty("is_duplicate")
        boolean isDuplicate,

        @JsonProperty("end_time_duplicate")
        OffsetDateTime endTimeToDuplicate,

        @JsonProperty("build_id")
        int buildId,

        @JsonProperty("room_id")
        int roomId
) {
}
