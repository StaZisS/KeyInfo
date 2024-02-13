package org.example.key_info.public_interface.client;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientProfileDto(
    UUID clientId,
    String name,
    String email,
    String gender
) {
}
