package org.example.key_info.rest.controller.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record RegisterDto(
        String name,
        String email,
        String password,
        String gender
) {
}
