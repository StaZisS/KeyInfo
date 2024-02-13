package org.example.key_info.rest.controller.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CreateTransferRequest(
        @JsonProperty("key_id")
        UUID keyId
) {
}
