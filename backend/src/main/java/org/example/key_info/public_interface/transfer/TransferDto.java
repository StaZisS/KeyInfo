package org.example.key_info.public_interface.transfer;

import org.example.key_info.core.transfer.TransferStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TransferDto(
        UUID transferId,
        UUID ownerId,
        UUID receiverId,
        TransferStatus status,
        OffsetDateTime createdTime,
        UUID keyId,
        String ownerName,
        String ownerEmail,
        String receiverName,
        String receiverEmail,
        int buildId,
        int roomId
) {
}
