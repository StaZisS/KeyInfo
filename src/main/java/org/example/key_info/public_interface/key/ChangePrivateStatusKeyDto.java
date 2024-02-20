package org.example.key_info.public_interface.key;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;
import java.util.UUID;

public record ChangePrivateStatusKeyDto(
        UUID clientId,
        Set<ClientRole> roles,
        UUID keyId,
        boolean isPrivate
) {
}
