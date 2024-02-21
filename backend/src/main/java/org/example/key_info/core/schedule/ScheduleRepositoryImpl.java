package org.example.key_info.core.schedule;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.application.ApplicationStatus;
import org.example.key_info.public_interface.schedule.GetFreeAudienceDto;
import org.example.key_info.public_interface.schedule.GetFreeTimeSlotsDto;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

import static com.example.shop.public_.Tables.TIMESLOT;
import static com.example.shop.public_.tables.Request.REQUEST;
import static com.example.shop.public_.tables.Studyroom.STUDYROOM;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;

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

    @Override
    public List<AudienceEntity> getFreeAudience(GetFreeAudienceDto dto) {
        Condition conditionStudyRoom= DSL.trueCondition();
        Condition conditionRequest = DSL.trueCondition();

        if (dto.buildId() != null) {
            conditionStudyRoom = conditionStudyRoom.and(STUDYROOM.BUILD.eq(dto.buildId()));
            conditionRequest  = conditionRequest.and(REQUEST.BUILD.eq(dto.buildId()));
        }

        if (dto.roomId() != null) {
            conditionStudyRoom = conditionStudyRoom.and(STUDYROOM.ROOM.eq(dto.roomId()));
            conditionRequest  = conditionRequest.and(REQUEST.ROOM.eq(dto.roomId()));
        }

        return Stream.concat(
                create.selectFrom(STUDYROOM)
                        .where(DSL.row(STUDYROOM.BUILD, STUDYROOM.ROOM).notIn(
                                select(REQUEST.BUILD, REQUEST.ROOM)
                                        .from(REQUEST)
                                        .where(REQUEST.START_TIME.eq(dto.startTime()))
                                        .and(REQUEST.END_TIME.eq(dto.endTime()))
                                        .and(REQUEST.STATUS.ne(ApplicationStatus.ACCEPTED.name()))
                        )).and(conditionStudyRoom)
                        .fetch(r -> new AudienceEntity(
                                        r.getBuild(),
                                        r.getRoom(),
                                        dto.startTime(),
                                        dto.endTime(),
                                        AudienceStatus.FREE
                                )
                        ).stream(),
                create.selectFrom(REQUEST)
                        .where(REQUEST.START_TIME.eq(dto.startTime()))
                        .and(REQUEST.END_TIME.eq(dto.endTime()))
                        .and(REQUEST.STATUS.ne(ApplicationStatus.ACCEPTED.name()))
                        .and(conditionRequest)
                        .fetch(r -> new AudienceEntity(
                                r.getBuild(),
                                r.getRoom(),
                                dto.startTime(),
                                dto.endTime(),
                                AudienceStatus.OCCUPIED
                        )).stream()
        ).toList();
    }
}
