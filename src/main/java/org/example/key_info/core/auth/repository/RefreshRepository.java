package org.example.key_info.core.auth.repository;

import org.example.key_info.core.auth.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshRepository {
    void saveRefreshToken(RefreshTokenEntity entity);
    Optional<RefreshTokenEntity> getRefreshTokenById(String tokenId);
    void deleteRefreshToken(String tokenId);
}
