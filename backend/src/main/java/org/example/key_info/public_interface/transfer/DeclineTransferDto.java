package org.example.key_info.public_interface.transfer;

import java.util.UUID;

public record DeclineTransferDto(
        UUID clientId,
        UUID transferId
) {
}
