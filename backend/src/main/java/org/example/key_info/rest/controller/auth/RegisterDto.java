package org.example.key_info.rest.controller.auth;

public record RegisterDto(
        String name,
        String email,
        String password,
        String gender
) {
}
