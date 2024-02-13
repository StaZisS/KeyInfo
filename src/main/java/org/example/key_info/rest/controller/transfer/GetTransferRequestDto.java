package org.example.key_info.rest.controller.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record GetTransferRequestDto(
        @JsonProperty("key_id")
        UUID keyId,

        @JsonProperty("owner_id")
        UUID ownerId,

        @JsonProperty("receiver_id")
        UUID receiverId,

        String status
) {
}
