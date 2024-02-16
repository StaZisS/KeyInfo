package org.example.key_info.core.schedule;

import java.time.OffsetDateTime;

public record GetFreeTimeSlotsDto(
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        Integer buildId,
        Integer roomId
) {
}
