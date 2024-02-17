package org.example.key_info.core.key.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KeyRepository {
    List<KeyEntity> getMyKeys(UUID holderId);
    UUID createKey(KeyEntity entity);
    void deleteKey(UUID keyId);
    List<KeyEntity> getAllKeys(FilterKeyDto dto);
    List<KeyEntity> getAllKeys(Integer build, Integer room);
    Optional<KeyEntity> getKey(UUID keyId);
    void updateKey(KeyEntity entity);
    void updateKeyHolder(UUID keyId, UUID keyHolderId);
}
