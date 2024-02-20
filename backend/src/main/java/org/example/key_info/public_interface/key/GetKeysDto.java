package org.example.key_info.public_interface.key;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;
import java.util.UUID;

public record GetKeysDto(
        UUID clientId,
        Set<ClientRole> roles,
        String keyStatus,
        Integer buildId,
        Integer roomId,
        Boolean isPrivate
) {
}
