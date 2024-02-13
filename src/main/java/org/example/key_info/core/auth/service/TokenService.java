package org.example.key_info.core.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.auth.entity.RefreshTokenEntity;
import org.example.key_info.core.auth.provider.DataForGenerateToken;
import org.example.key_info.core.auth.provider.JwtProvider;
import org.example.key_info.core.auth.repository.RefreshRepository;
import org.example.key_info.public_interface.auth.JwtResponseDto;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshRepository refreshRepository;
    private final JwtProvider jwtProvider;

    public JwtResponseDto getTokens(DataForGenerateToken data) {
        final String accessToken = jwtProvider.generateAccessToken(data);
        final String refreshToken = jwtProvider.generateRefreshToken(data);

        final RefreshTokenEntity refreshTokenEntity = getRefreshTokenEntity(refreshToken);
        refreshRepository.saveRefreshToken(refreshTokenEntity);

        return new JwtResponseDto(accessToken, refreshToken);
    }

    public void checkRefreshToken(String refreshToken) {
        jwtProvider.validateRefreshToken(refreshToken);

        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final String tokenId = claims.getId();

        final RefreshTokenEntity oldRefreshToken = refreshRepository.getRefreshTokenById(tokenId)
                .orElseThrow(() -> new ExceptionInApplication("RefreshToken истек", ExceptionType.UNAUTHORIZED));
        if (!oldRefreshToken.getRefreshToken().equals(refreshToken)) {
            throw new ExceptionInApplication("Недействительный RefreshToken", ExceptionType.UNAUTHORIZED);
        }
    }

    public String getClientIdInRefreshToken(String refreshToken) {
        return jwtProvider.getRefreshClaims(refreshToken).getSubject();
    }

    public String getTokenIdInRefreshToken(String refreshToken) {
        return jwtProvider.getRefreshClaims(refreshToken).getId();
    }

    public JwtResponseDto getAccessToken(DataForGenerateToken data) {
        var accessToken = jwtProvider.generateAccessToken(data);
        return new JwtResponseDto(accessToken, null);
    }

    public void deleteRefreshToken(String tokenId) {
        refreshRepository.deleteRefreshToken(tokenId);
    }

    private RefreshTokenEntity getRefreshTokenEntity(String refreshToken) {
        return new RefreshTokenEntity(
                jwtProvider.getRefreshClaims(refreshToken).getId(),
                refreshToken,
                jwtProvider.getRefreshTokenTtl()
        );
    }
}
