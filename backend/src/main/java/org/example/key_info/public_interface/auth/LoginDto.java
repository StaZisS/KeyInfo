package org.example.key_info.public_interface.auth;

public record LoginDto(
        String email,
        String password
) {
}
