package org.example.key_info.public_interface.auth;

public record JwtResponseDto(
        String accessToken,
        String refreshToken
){
}
