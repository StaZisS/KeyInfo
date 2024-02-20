package org.example.key_info.core.role;

import com.example.shop.public_.tables.records.RoleRecord;
import org.example.key_info.core.client.repository.ClientRole;
import org.jetbrains.annotations.Nullable;
import org.jooq.RecordMapper;

public class RoleEntityMapper implements RecordMapper<RoleRecord, ClientRole> {

    @Override
    public @Nullable ClientRole map(RoleRecord roleRecord) {
        return ClientRole.valueOf(roleRecord.getRole());
    }
}
