package org.example.key_info.public_interface.application;

import org.example.key_info.core.client.repository.ClientRole;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public record CreateApplicationDto(
        UUID clientId,
        Set<ClientRole> clientRoles,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        int buildId,
        int roomId,
        String role,
        boolean isDuplicate,
        OffsetDateTime endTimeToDuplicate
) {
}
