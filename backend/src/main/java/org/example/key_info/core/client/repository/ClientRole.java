package org.example.key_info.core.client.repository;

import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;

public enum ClientRole {
    ADMIN,
    STUDENT,
    TEACHER,
    DEANERY,
    UNSPECIFIED,
    ;

    public static ClientRole getClientRoleByName(String name) {
        try {
            return ClientRole.valueOf(name);
        } catch (Exception e) {
            throw new ExceptionInApplication("Такой роли не существует", ExceptionType.NOT_FOUND);
        }
    }
}
