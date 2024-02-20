package org.example.key_info.public_interface.transfer;

import java.util.UUID;

public record GetMyTransfersDto(
        UUID clientId,
        String status
) {
}
