package org.example.key_info.public_interface.client;

import java.time.OffsetDateTime;

public record ClientCreateDto(
        String name,
        String email,
        String password,
        String gender
) {
}
