package org.example.key_info.public_interface.key;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;
import java.util.UUID;

public record KeyDeleteDto(
        UUID clientId,
        Set<ClientRole> clientRoles,
        UUID keyId
) {
}
