package org.example.key_info.public_interface.deaneries;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;
import java.util.UUID;

public record AcceptKeyDeaneriesDto(
        UUID clientId,
        Set<ClientRole> clientRoles,
        UUID holderId,
        UUID keyId
) {
}
