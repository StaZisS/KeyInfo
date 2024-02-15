package org.example.key_info.rest.controller.role;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;

public record UpdateClientRoles(
        Set<ClientRole> clientRoles
) {
}
