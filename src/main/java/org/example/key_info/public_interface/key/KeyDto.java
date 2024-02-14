package org.example.key_info.public_interface.key;

import org.example.key_info.core.key.repository.KeyStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record KeyDto(
        UUID keyId,
        KeyStatus status,
        UUID keyHolderId,
        int roomId,
        int buildId,
        OffsetDateTime lastAccess
){
}
