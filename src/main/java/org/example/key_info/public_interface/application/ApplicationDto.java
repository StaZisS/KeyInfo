package org.example.key_info.public_interface.application;

import org.example.key_info.core.application.ApplicationStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ApplicationDto(
        UUID applicationId,
        UUID applicationCreatorId,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        ApplicationStatus status,
        OffsetDateTime createdTime,
        int buildId,
        int roomId,
        boolean isDuplicate,
        OffsetDateTime endTimeToDuplicate,
        String clientName,
        String clientEmail
) {
}
