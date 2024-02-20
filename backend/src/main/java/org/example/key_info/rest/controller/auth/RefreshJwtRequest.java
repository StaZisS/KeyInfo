package org.example.key_info.rest.controller.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshJwtRequest(
        @JsonProperty("refresh_token")
        String refreshToken
) {
}
