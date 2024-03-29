package org.example.key_info.core.transfer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransferRepository {
    UUID createTransfer(TransferEntity entity);
    Optional<TransferEntity> getTransferById(UUID transferId);
    void deleteTransfer(UUID transferId);
    void updateTransfer(TransferEntity entity);
    List<TransferEntity> getMyTransfers(UUID clientId, TransferStatus status);
    void declineNotActualTransfers(UUID keyId);
    boolean isNotDuplicate(UUID ownerId, UUID receiverId, UUID keyId);
    List<TransferEntity> getForeignTransfers(UUID clientId, TransferStatus status);
}
