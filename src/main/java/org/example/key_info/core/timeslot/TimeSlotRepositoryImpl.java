package org.example.key_info.core.timeslot;

import com.example.shop.public_.tables.records.TimeslotRecord;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.schedule.TimeSlotEntity;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetTime;
import java.util.Optional;

import static com.example.shop.public_.tables.Timeslot.TIMESLOT;

@Repository
@RequiredArgsConstructor
public class TimeSlotRepositoryImpl implements TimeSlotRepository {
    private final TimeSlotEntityMapper timeSlotEntityMapper = new TimeSlotEntityMapper();
    private final DSLContext create;

    @Override
    public void createTimeslot(TimeSlotEntity entity) {
        create.insertInto(TIMESLOT)
                .set(TIMESLOT.START_TIME, entity.startTime())
                .set(TIMESLOT.END_TIME, entity.endTime())
                .execute();
    }

    @Override
    public Optional<TimeSlotEntity> getTimeslot(TimeSlotEntity timeSlotEntity) {
        return create.selectFrom(TIMESLOT)
                .where(TIMESLOT.START_TIME.eq(timeSlotEntity.startTime()))
                .and(TIMESLOT.END_TIME.eq(timeSlotEntity.endTime()))
                .fetchOptional(timeSlotEntityMapper);
    }
}
