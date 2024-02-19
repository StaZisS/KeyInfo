package org.example.key_info.rest.controller.deanery;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.key_info.public_interface.client.ClientProfileDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ResponseKeyDto(
        @JsonProperty("key_id")
        UUID keyId,
        int build,
        int room,

        @JsonProperty("key_status")
        String keyStatus,

        @JsonProperty("last_access")
        OffsetDateTime lastAccess,

        ClientProfileDto client
) {
}
