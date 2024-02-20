package org.example.key_info.public_interface.schedule;

import java.time.OffsetDateTime;

public record GetFreeAudienceDto(
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        Integer buildId,
        Integer roomId
) {
}
