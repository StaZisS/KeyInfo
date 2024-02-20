package org.example.key_info.core.schedule;

import java.time.OffsetDateTime;

public record AudienceEntity(
        int buildId,
        int roomId,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        AudienceStatus status
) {
}
