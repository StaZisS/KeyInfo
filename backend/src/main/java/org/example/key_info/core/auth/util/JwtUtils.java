package org.example.key_info.core.auth.util;

import io.jsonwebtoken.Claims;
import org.example.key_info.core.auth.JwtAuthentication;
import org.example.key_info.core.auth.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtUtils {
    private static final String ROLES_NAME =  "Roles";
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get(ROLES_NAME, List.class);
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}
