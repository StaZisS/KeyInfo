package org.example.key_info.core.schedule;

import org.example.key_info.public_interface.schedule.GetFreeAudienceDto;
import org.example.key_info.public_interface.schedule.GetFreeTimeSlotsDto;

import java.util.List;

public interface ScheduleRepository {
    List<TimeSlotEntity> getFreeTimeSlots(GetFreeTimeSlotsDto getFreeTimeSlotsDto);
    List<AudienceEntity> getFreeAudience(GetFreeAudienceDto dto);
}
