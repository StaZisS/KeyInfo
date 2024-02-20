package org.example.key_info.public_interface.key;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;
import java.util.UUID;

public record KeyCreateDto(
        UUID clientId,
        Set<ClientRole> clientRoles,
        int roomId,
        int buildId,
        boolean isPrivate
) {
}
