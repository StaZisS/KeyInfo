package org.example.key_info.core.user;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.Set;

public record GetUsersDto(
        Set<ClientRole> clientRoles,
        String name,
        String email
) {
}
