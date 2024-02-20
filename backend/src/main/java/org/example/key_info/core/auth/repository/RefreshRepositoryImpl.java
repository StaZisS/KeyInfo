package org.example.key_info.core.auth.repository;

import lombok.NonNull;
import org.example.key_info.core.auth.entity.RefreshTokenEntity;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshRepositoryImpl implements RefreshRepository {
    private static final String KEY = "RefreshToken";

    private final HashOperations<String, String, RefreshTokenEntity> hashOperations;

    public RefreshRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveRefreshToken(@NonNull RefreshTokenEntity entity) {
        hashOperations.put(KEY, entity.getTokenId(), entity);
    }

    @Override
    public Optional<RefreshTokenEntity> getRefreshTokenById(@NonNull String tokenId) {
        return Optional.ofNullable(hashOperations.get(KEY, tokenId));
    }

    @Override
    public void deleteRefreshToken(@NonNull String tokenId) {
        hashOperations.delete(KEY, tokenId);
    }
}
