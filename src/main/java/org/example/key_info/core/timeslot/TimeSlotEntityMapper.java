package org.example.key_info.core.timeslot;

import com.example.shop.public_.tables.records.TimeslotRecord;
import org.example.key_info.core.schedule.TimeSlotEntity;
import org.jetbrains.annotations.Nullable;
import org.jooq.RecordMapper;

import java.time.OffsetDateTime;
import java.util.TimeZone;

public class TimeSlotEntityMapper implements RecordMapper<TimeslotRecord, TimeSlotEntity> {

    @Override
    public @Nullable TimeSlotEntity map(TimeslotRecord timeSlotEntity) {
        return new TimeSlotEntity(
                timeSlotEntity.getStartTime(),
                timeSlotEntity.getEndTime()
        );
    }
}
