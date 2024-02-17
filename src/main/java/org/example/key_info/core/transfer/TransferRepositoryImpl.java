package org.example.key_info.core.transfer;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.shop.public_.tables.Transferrequest.TRANSFERREQUEST;

@Repository
@RequiredArgsConstructor
public class TransferRepositoryImpl implements TransferRepository {
    private final DSLContext create;

    private final TransferEntityMapper transferEntityMapper = new TransferEntityMapper();

    @Override
    public UUID createTransfer(TransferEntity entity) {
        return create.insertInto(TRANSFERREQUEST)
                .set(TRANSFERREQUEST.OWNER_ID, entity.ownerId())
                .set(TRANSFERREQUEST.RECEIVER_ID, entity.receiverId())
                .set(TRANSFERREQUEST.STATUS, entity.status().name())
                .set(TRANSFERREQUEST.CREATED_TIME, entity.createdTime())
                .set(TRANSFERREQUEST.KEY_ID, entity.keyId())
                .returning(TRANSFERREQUEST.TRANSFER_REQUEST_ID)
                .fetchOne(TRANSFERREQUEST.TRANSFER_REQUEST_ID);
    }

    @Override
    public Optional<TransferEntity> getTransferById(UUID transferId) {
        return create.selectFrom(TRANSFERREQUEST)
                .where(TRANSFERREQUEST.TRANSFER_REQUEST_ID.eq(transferId))
                .fetchOptional(transferEntityMapper);
    }

    @Override
    public void deleteTransfer(UUID transferId) {
        create.deleteFrom(TRANSFERREQUEST)
                .where(TRANSFERREQUEST.TRANSFER_REQUEST_ID.eq(transferId))
                .execute();
    }

    @Override
    public void updateTransfer(TransferEntity entity) {
        create.update(TRANSFERREQUEST)
                .set(TRANSFERREQUEST.OWNER_ID, entity.ownerId())
                .set(TRANSFERREQUEST.RECEIVER_ID, entity.receiverId())
                .set(TRANSFERREQUEST.STATUS, entity.status().name())
                .set(TRANSFERREQUEST.CREATED_TIME, entity.createdTime())
                .set(TRANSFERREQUEST.KEY_ID, entity.keyId())
                .where(TRANSFERREQUEST.TRANSFER_REQUEST_ID.eq(entity.transferId()))
                .execute();
    }

    @Override
    public List<TransferEntity> getMyTransfers(UUID clientId, TransferStatus status) {
        return create.selectFrom(TRANSFERREQUEST)
                .where(TRANSFERREQUEST.OWNER_ID.eq(clientId))
                .and(TRANSFERREQUEST.STATUS.eq(status.name()))
                .fetch(transferEntityMapper);
    }

    @Override
    public void declineNotActualTransfers(UUID keyId) {
        create.update(TRANSFERREQUEST)
                .set(TRANSFERREQUEST.STATUS, TransferStatus.DECLINED.name())
                .where(TRANSFERREQUEST.KEY_ID.eq(keyId))
                .and(TRANSFERREQUEST.STATUS.eq(TransferStatus.IN_PROCESS.name()))
                .execute();
    }

    @Override
    public boolean isNotDuplicate(UUID ownerId, UUID receiverId, UUID keyId) {
        return create.selectFrom(TRANSFERREQUEST)
                .where(TRANSFERREQUEST.OWNER_ID.eq(ownerId))
                .and(TRANSFERREQUEST.RECEIVER_ID.eq(receiverId))
                .and(TRANSFERREQUEST.KEY_ID.eq(keyId))
                .and(TRANSFERREQUEST.STATUS.eq(TransferStatus.IN_PROCESS.name()))
                .fetch(transferEntityMapper)
                .isEmpty();
    }

    @Override
    public List<TransferEntity> getForeignTransfers(UUID clientId, TransferStatus status) {
        return create.selectFrom(TRANSFERREQUEST)
                .where(TRANSFERREQUEST.RECEIVER_ID.eq(clientId))
                .and(TRANSFERREQUEST.STATUS.eq(status.name()))
                .fetch(transferEntityMapper);
    }
}
