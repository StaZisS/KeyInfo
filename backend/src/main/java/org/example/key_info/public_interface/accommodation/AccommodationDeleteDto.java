package org.example.key_info.public_interface.accommodation;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;
import java.util.UUID;

public record AccommodationDeleteDto(
        UUID clientId,
        Set<ClientRole> clientRoles,
        int buildId,
        int roomId
) {
}
