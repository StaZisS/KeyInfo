package org.example.key_info.core.profile;

import java.util.UUID;

public record UpdateProfileDto(
        UUID userId,
        String name,
        String email,
        String password
) {
}
