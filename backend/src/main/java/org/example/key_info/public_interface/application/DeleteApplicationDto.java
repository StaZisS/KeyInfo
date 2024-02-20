package org.example.key_info.public_interface.application;

import java.util.UUID;

public record DeleteApplicationDto(
        UUID clientId,
        UUID applicationId
) {
}
