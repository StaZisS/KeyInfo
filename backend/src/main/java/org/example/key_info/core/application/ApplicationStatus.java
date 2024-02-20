package org.example.key_info.core.application;

import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;

public enum ApplicationStatus {
    IN_PROCESS,
    ACCEPTED,
    DECLINED,
    ;

    public static ApplicationStatus getApplicationStatusByName(String name) {
        try {
            return ApplicationStatus.valueOf(name);
        } catch (Exception e) {
            throw new ExceptionInApplication("Такого статуса нет у заявки", ExceptionType.INVALID);
        }
    }
}
