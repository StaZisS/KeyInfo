package org.example.key_info.public_interface.transfer;

import java.util.UUID;

public record CreateTransferDto(
        UUID ownerId,
        UUID receiverId,
        UUID keyId
) {
}
