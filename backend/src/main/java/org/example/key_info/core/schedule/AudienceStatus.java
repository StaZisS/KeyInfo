package org.example.key_info.core.schedule;

import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;

public enum AudienceStatus {
    FREE,
    OCCUPIED,
    ;

    public static AudienceStatus getAudienceStatusByName(String name) {
        try {
            return AudienceStatus.valueOf(name);
        } catch (Exception e) {
            throw new ExceptionInApplication("Такого статуса у аудитории нет", ExceptionType.NOT_FOUND);
        }
    }
}
