package org.example.key_info.core.key.repository;

import java.time.OffsetDateTime;
import java.util.UUID;

public record KeyEntity(
        UUID keyId,
        KeyStatus status,
        UUID keyHolderId,
        int roomId,
        int buildId,
        OffsetDateTime lastAccess,
        boolean isPrivate
) {
}
