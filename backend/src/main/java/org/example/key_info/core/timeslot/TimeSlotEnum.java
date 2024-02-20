package org.example.key_info.core.timeslot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeSlotEnum {
    FIRST(8, 45, 10, 20),
    SECOND(10,35,12,10),
    THIRD(12,25,14,0),
    FOURTH(14,45,16,20),
    FIFTH(16,35,18,10),
    SIXTH(18,25,20,0),
    SEVENTH(20,15,21,50);

    private final int startHour;
    private final int startMinute;
    private final int endHour;
    private final int endMinute;
}
