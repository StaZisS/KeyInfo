package org.example.key_info.public_interface.application;

import org.example.key_info.core.client.repository.ClientRole;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public record UpdateApplicationDto(
        UUID clientId,
        Set<ClientRole> clientRoles,
        UUID applicationId,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        int buildId,
        int roomId,
        String role,
        boolean isDuplicate,
        OffsetDateTime endTimeToDuplicate
) {
}
