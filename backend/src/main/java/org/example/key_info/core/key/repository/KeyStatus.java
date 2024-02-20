package org.example.key_info.core.key.repository;

import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;

public enum KeyStatus {
    IN_DEANERY,
    IN_HAND,
    ;

    public static KeyStatus getKeyStatusByName(String name) {
        try {
            return KeyStatus.valueOf(name);
        } catch (Exception e) {
            throw new ExceptionInApplication("Такого статуса у ключа не существует", ExceptionType.NOT_FOUND);
        }
    }
}
