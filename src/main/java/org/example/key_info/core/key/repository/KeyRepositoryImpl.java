package org.example.key_info.core.key.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.example.shop.public_.tables.Key.KEY;

@Repository
@RequiredArgsConstructor
public class KeyRepositoryImpl implements KeyRepository {
    private final DSLContext create;

    private final KeyEntityMapper keyEntityMapper = new KeyEntityMapper();

    @Override
    public List<KeyEntity> getMyKeys(UUID holderId) {
        return create.selectFrom(KEY)
                .where(KEY.KEY_HOLDER_ID.eq(holderId))
                .fetchStream()
                .map(keyEntityMapper)
                .toList();
    }

    @Override
    public UUID createKey(KeyEntity entity) {
        return create.insertInto(KEY)
                .set(KEY.STATUS, entity.status().name())
                .set(KEY.KEY_HOLDER_ID, entity.keyHolderId())
                .set(KEY.ROOM, entity.roomId())
                .set(KEY.BUILD, entity.buildId())
                .set(KEY.LAST_ACCESS, entity.lastAccess())
                .returning(KEY.KEY_ID)
                .fetchOne(KEY.KEY_ID);
    }

    @Override
    public void deleteKey(UUID keyId) {
        create.deleteFrom(KEY)
                .where(KEY.KEY_ID.eq(keyId))
                .execute();
    }
}
