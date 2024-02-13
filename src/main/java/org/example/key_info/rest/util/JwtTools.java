package org.example.key_info.rest.util;

public class JwtTools {
    private static final String PREFIX = "Bearer ";
    public static String getTokenFromHeader(String rawToken) {
        return rawToken.replaceFirst(PREFIX, "");
    }
}
