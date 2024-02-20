package org.example.key_info.core.role;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;
import java.util.UUID;

public record UserStatusUpdateDto(
        UUID userId,
        Set<ClientRole> clientRoles,
        UUID appointUserId,
        Set<ClientRole> appointUserRoles
) {
}
