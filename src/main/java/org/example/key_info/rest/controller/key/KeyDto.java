package org.example.key_info.rest.controller.key;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public record KeyDto(
        @JsonProperty("key_id")
        UUID keyId,
        int build,
        int room,

        @JsonProperty("last_access")
        OffsetDateTime lastAccess
) {
}
