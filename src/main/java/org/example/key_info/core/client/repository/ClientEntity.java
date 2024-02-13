package org.example.key_info.core.client.repository;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientEntity(
        UUID clientId,
        String name,
        String email,
        String password,
        String gender,
        OffsetDateTime createdDate,
        ClientRole role
) {
}
