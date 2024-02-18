package org.example.key_info.rest.controller.transfer;

import java.util.UUID;

public record CreateTransferResponseDto(
        UUID uuid,
        int build,
        int room
) {
}
