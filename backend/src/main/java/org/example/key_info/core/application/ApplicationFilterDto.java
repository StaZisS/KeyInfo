package org.example.key_info.core.application;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record ApplicationFilterDto(
        ApplicationStatus status,
        OffsetDateTime start,
        OffsetDateTime end,
        Integer buildId,
        Integer roomId
) {
}
