package org.example.key_info.core.application;

import java.time.OffsetDateTime;

public record ApplicationFilterDto(
        ApplicationStatus status,
        OffsetDateTime start,
        OffsetDateTime end,
        Integer buildId,
        Integer roomId
) {
}
