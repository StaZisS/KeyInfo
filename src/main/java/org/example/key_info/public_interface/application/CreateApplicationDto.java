package org.example.key_info.public_interface.application;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;
import java.util.UUID;

public record CreateApplicationDto(
        UUID clientId,
        Set<ClientRole> roles

) {
}
