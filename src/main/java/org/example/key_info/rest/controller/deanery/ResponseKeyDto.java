package org.example.key_info.rest.controller.deanery;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ResponseKeyDto(
        @JsonProperty("key_id")
        UUID keyId,

        @JsonProperty("key_status")
        String keyStatus,

        @JsonProperty("key_holder_id")
        UUID keyHolderId,

        @JsonProperty("key_holder_name")
        String keyHolderName,

        @JsonProperty("key_holder_email")
        String keyHolderEmail,

        int build,
        int room
) {
}
