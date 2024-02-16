package org.example.key_info.core.schedule;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.shop.public_.Tables.TIMESLOT;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final DSLContext create;

    @Override
    public List<TimeSlotEntity> getFreeTimeSlots(GetFreeTimeSlotsDto dto) {
        var query = create.selectFrom(TIMESLOT);

        Condition condition = DSL.trueCondition();

        if (dto.startTime() != null) {
            condition = condition.and(TIMESLOT.START_TIME.greaterOrEqual(dto.startTime()));
        }

        if (dto.endTime() != null) {
            condition = condition.and(TIMESLOT.END_TIME.lessOrEqual(dto.endTime()));
        }

        return query.where(condition)
                .fetchStream()
                .map(record -> new TimeSlotEntity(
                        record.getStartTime(),
                        record.getEndTime()
                ))
                .toList();
    }
}
