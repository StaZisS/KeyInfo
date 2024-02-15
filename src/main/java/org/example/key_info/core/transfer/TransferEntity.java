package org.example.key_info.core.transfer;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TransferEntity(
        UUID transferId,
        UUID ownerId,
        UUID receiverId,
        TransferStatus status,
        OffsetDateTime createdTime,
        UUID keyId
) {
}
