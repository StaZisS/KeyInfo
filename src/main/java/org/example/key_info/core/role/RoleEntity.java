package org.example.key_info.core.role;

import java.util.UUID;

public record RoleEntity(
        UUID userId,
        String role
) {
}
