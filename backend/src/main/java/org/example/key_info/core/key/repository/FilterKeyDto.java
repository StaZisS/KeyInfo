package org.example.key_info.core.key.repository;

public record FilterKeyDto(
        KeyStatus status,
        Integer buildId,
        Integer roomId,
        Boolean isPrivate
) {
}
