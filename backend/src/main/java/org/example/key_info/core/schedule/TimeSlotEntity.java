package org.example.key_info.core.schedule;

import java.time.OffsetDateTime;

public record TimeSlotEntity(
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {
}
