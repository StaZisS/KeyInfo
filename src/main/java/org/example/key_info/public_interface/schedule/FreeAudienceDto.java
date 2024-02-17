package org.example.key_info.public_interface.schedule;

import java.time.OffsetDateTime;

public record FreeAudienceDto(
        int buildId,
        int roomId,
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {
}
