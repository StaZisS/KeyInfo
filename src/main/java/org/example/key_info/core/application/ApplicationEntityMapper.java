package org.example.key_info.core.application;

import com.example.shop.public_.tables.records.RequestRecord;
import org.jooq.RecordMapper;

public class ApplicationEntityMapper implements RecordMapper<RequestRecord, ApplicationEntity> {
    @Override
    public ApplicationEntity map(RequestRecord requestRecord) {
        return new ApplicationEntity(
                requestRecord.getRequestId(),
                requestRecord.getRequestCreator(),
                requestRecord.getStartTime(),
                requestRecord.getEndTime(),
                ApplicationStatus.getApplicationStatusByName(requestRecord.getStatus()),
                requestRecord.getCreatedTime(),
                requestRecord.getBuild(),
                requestRecord.getRoom()
        );
    }
}
