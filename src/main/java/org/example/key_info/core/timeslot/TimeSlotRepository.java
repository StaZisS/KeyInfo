package org.example.key_info.core.timeslot;

import org.example.key_info.core.schedule.TimeSlotEntity;

import java.util.Optional;

public interface TimeSlotRepository {
    void createTimeslot(TimeSlotEntity entity);
    Optional<TimeSlotEntity> getTimeslot(TimeSlotEntity entity);
}
