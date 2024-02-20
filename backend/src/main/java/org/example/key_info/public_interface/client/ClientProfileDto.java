package org.example.key_info.public_interface.client;

import org.example.key_info.core.client.repository.ClientRole;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public record ClientProfileDto(
    UUID clientId,
    String name,
    String email,
    String gender,
    OffsetDateTime createdDate,
    Set<ClientRole> roles
) {
}
