package org.example.key_info.core.key.repository;

import java.util.List;
import java.util.UUID;

public interface KeyRepository {
    List<KeyEntity> getMyKeys(UUID holderId);
    UUID createKey(KeyEntity entity);
    void deleteKey(UUID keyId);
}
