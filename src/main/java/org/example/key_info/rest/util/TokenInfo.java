package org.example.key_info.rest.util;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;
import java.util.UUID;

public record TokenInfo(
        UUID clientId,
        Set<ClientRole> clientRoles
) {
}
