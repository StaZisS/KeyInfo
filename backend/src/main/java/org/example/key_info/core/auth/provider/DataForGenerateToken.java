package org.example.key_info.core.auth.provider;

import org.example.key_info.core.auth.Role;

import java.util.Set;

public record DataForGenerateToken(
        String clientId,
        Set<Role> role
) {
}
