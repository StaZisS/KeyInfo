package org.example.key_info.rest.controller.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record TransferResponseDto(
        @JsonProperty("key_id")
        UUID keyId,

        @JsonProperty("owner_id")
        UUID ownerId,

        @JsonProperty("receiver_id")
        UUID receiverId,

        String status,

        @JsonProperty("owner_name")
        String nameOwner,

        @JsonProperty("owner_email")
        String ownerEmail,

        @JsonProperty("receiver_name")
        String nameReceiver,

        @JsonProperty("receiver_email")
        String emailReceiver,

        @JsonProperty("transfer_id")
        UUID transferId
) {
}
