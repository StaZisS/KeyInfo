package org.example.key_info.core.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordTool {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static String getHashPassword(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

    public static boolean isCorrectPassword(String hashPassword, String rawPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, hashPassword);
    }
}
