package org.example.key_info.core.auth.provider;

import org.example.key_info.core.auth.Role;

public record DataForGenerateToken(
        String clientId,
        Role role
) {
}
