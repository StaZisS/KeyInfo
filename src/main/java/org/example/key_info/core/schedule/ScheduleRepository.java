package org.example.key_info.core.schedule;

import java.util.List;

public interface ScheduleRepository {
    List<TimeSlotEntity> getFreeTimeSlots(GetFreeTimeSlotsDto getFreeTimeSlotsDto);
}
