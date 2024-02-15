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
}
