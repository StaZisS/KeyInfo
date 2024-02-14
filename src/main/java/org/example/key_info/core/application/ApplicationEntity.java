package org.example.key_info.core.application;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ApplicationEntity(
        UUID applicationId,
        UUID applicationCreatorId,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        ApplicationStatus status,
        OffsetDateTime createdTime,
        int buildId,
        int roomId,
        boolean isDuplicate,
        OffsetDateTime endTimeToDuplicate
) {
}
