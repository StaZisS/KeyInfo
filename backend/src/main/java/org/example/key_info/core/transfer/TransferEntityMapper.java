package org.example.key_info.core.transfer;

import com.example.shop.public_.tables.records.TransferrequestRecord;
import org.jooq.RecordMapper;

public class TransferEntityMapper implements RecordMapper<TransferrequestRecord, TransferEntity> {
    @Override
    public TransferEntity map(TransferrequestRecord record) {
        return new TransferEntity(
                record.getTransferRequestId(),
                record.getOwnerId(),
                record.getReceiverId(),
                TransferStatus.getTransferStatusByName(record.getStatus()),
                record.getCreatedTime(),
                record.getKeyId()
        );
    }
}
