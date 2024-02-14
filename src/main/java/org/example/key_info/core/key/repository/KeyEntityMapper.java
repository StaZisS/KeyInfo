package org.example.key_info.core.key.repository;

import com.example.shop.public_.tables.records.KeyRecord;
import org.jetbrains.annotations.Nullable;
import org.jooq.RecordMapper;

public class KeyEntityMapper implements RecordMapper<KeyRecord, KeyEntity> {
    @Override
    public @Nullable KeyEntity map(KeyRecord record) {
        return new KeyEntity(
                record.getKeyId(),
                KeyStatus.getKeyStatusByName(record.getStatus()),
                record.getKeyHolderId(),
                record.getRoom(),
                record.getBuild(),
                record.getLastAccess()
        );
    }
}
