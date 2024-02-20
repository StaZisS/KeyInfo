package org.example.key_info.application;

public enum TimeSlotEnum {
    FIRST(8, 45, 10, 20),
    SECOND(10,35,12,10),
    THIRD(12,25,14,0),
    FOURTH(14,45,16,20),
    FIFTH(16,35,18,10),
    SIXTH(18,25,20,0),
    SEVENTH(20,15,21,50);

    TimeSlotEnum(int startHour, int startMinute, int endHour, int endMinute) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public final int startHour;
    public final int startMinute;
    public final int endHour;
    public final int endMinute;
}
