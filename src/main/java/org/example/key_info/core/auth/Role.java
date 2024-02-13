package org.example.key_info.core.auth;

import lombok.RequiredArgsConstructor;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN("ADMIN"),
    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    DEANERY("DEANERY")
    ;

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }

    public static Role getRoleByName(String name) {
        try {
            return Role.valueOf(name);
        } catch (Exception e) {
            throw new ExceptionInApplication("Такой роли не существует", ExceptionType.NOT_FOUND);
        }
    }
}
