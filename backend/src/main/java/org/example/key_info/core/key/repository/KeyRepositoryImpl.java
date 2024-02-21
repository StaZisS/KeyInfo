package org.example.key_info.core.key.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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
                .set(KEY.IS_PRIVATE, entity.isPrivate())
                .returning(KEY.KEY_ID)
                .fetchOne(KEY.KEY_ID);
    }

    @Override
    public void deleteKey(UUID keyId) {
        create.deleteFrom(KEY)
                .where(KEY.KEY_ID.eq(keyId))
                .execute();
    }

    @Override
    public List<KeyEntity> getAllKeys(FilterKeyDto dto) {
        var query = create.selectFrom(KEY);

        Condition condition = DSL.trueCondition();

        if (dto.status() != null) {
            condition = condition.and(KEY.STATUS.eq(dto.status().name()));
        }

        if (dto.buildId() != null) {
            condition = condition.and(KEY.BUILD.eq(dto.buildId()));
        }
        if (dto.roomId() != null) {
            condition = condition.and(KEY.ROOM.eq(dto.roomId()));
        }

        if (dto.isPrivate() != null) {
            condition = condition.and(KEY.IS_PRIVATE.eq(dto.isPrivate()));
        }

        return query.where(condition)
                .fetchStream()
                .map(keyEntityMapper)
                .toList();
    }

    @Override
    public List<KeyEntity> getAllKeys(Integer build, Integer room, Boolean isPrivate) {
        var query = create.selectFrom(KEY);

        Condition condition = DSL.trueCondition();

        if (build != null) {
            condition = condition.and(KEY.BUILD.eq(build));
        }

        if (room != null) {
            condition = condition.and(KEY.ROOM.eq(room));
        }

        if (isPrivate != null) {
            condition = condition.and(KEY.IS_PRIVATE.eq(isPrivate));
        }

        return query.where(condition)
                .fetchStream()
                .map(keyEntityMapper)
                .toList();
    }

    @Override
    public Optional<KeyEntity> getKey(UUID keyId) {
        return create.selectFrom(KEY)
                .where(KEY.KEY_ID.eq(keyId))
                .fetchOptional(keyEntityMapper);
    }

    @Override
    public void updateKey(KeyEntity entity) {
        create.update(KEY)
                .set(KEY.STATUS, entity.status().name())
                .set(KEY.KEY_HOLDER_ID, entity.keyHolderId())
                .set(KEY.ROOM, entity.roomId())
                .set(KEY.BUILD, entity.buildId())
                .set(KEY.LAST_ACCESS, entity.lastAccess())
                .set(KEY.IS_PRIVATE, entity.isPrivate())
                .where(KEY.KEY_ID.eq(entity.keyId()))
                .execute();
    }

    @Override
    public void updateKeyHolder(UUID keyId, UUID keyHolderId) {
        create.update(KEY)
                .set(KEY.KEY_HOLDER_ID, keyHolderId)
                .where(KEY.KEY_ID.eq(keyId))
                .execute();
    }
}
