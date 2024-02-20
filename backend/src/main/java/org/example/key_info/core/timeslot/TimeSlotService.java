package org.example.key_info.core.timeslot;

import lombok.AllArgsConstructor;
import org.example.key_info.core.schedule.TimeSlotEntity;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    public void createTimeslot(TimeSlotEntity entity) {
        var timeslot = timeSlotRepository.getTimeslot(entity);

        if (!timeslot.isPresent() && validateTimeSlot(entity)) {
            timeSlotRepository.createTimeslot(entity);
        } else if (!timeslot.isPresent()) {
            throw new ExceptionInApplication("Время не соответствует временным слотам", ExceptionType.INVALID);        }
    }

    private boolean validateTimeSlot(TimeSlotEntity entity) {
        for (TimeSlotEnum timeSlotEnum : TimeSlotEnum.values()) {
            if (isMatchingTimeSlot(entity, timeSlotEnum)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMatchingTimeSlot(TimeSlotEntity entity, TimeSlotEnum timeSlotEnum) {
        return entity.startTime().getHour() == timeSlotEnum.getStartHour() &&
                entity.startTime().getMinute() == timeSlotEnum.getStartMinute() &&
                entity.endTime().getHour() == timeSlotEnum.getEndHour() &&
                entity.endTime().getMinute() == timeSlotEnum.getEndMinute();
    }
}
