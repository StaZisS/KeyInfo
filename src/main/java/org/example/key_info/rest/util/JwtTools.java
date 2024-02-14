package org.example.key_info.rest.util;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.auth.provider.JwtProvider;
import org.example.key_info.core.client.repository.ClientRole;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTools {
    private static final String PREFIX = "Bearer ";
    private static final String ROLES_NAME =  "Roles";

    private final JwtProvider jwtProvider;

    public TokenInfo getClientInfoFromAccessToken(String rawAccessToken) {
        var token = getTokenFromHeader(rawAccessToken);
        final var claims = jwtProvider.getAccessClaims(token);

        var clientId = UUID.fromString(claims.getSubject());
        final List<String> stringRoles = claims.get(ROLES_NAME, List.class);
        var clientRoles = stringRoles.stream()
                .map(ClientRole::getClientRoleByName)
                .collect(Collectors.toSet());

        return new TokenInfo(clientId, clientRoles);
    }

    private String getTokenFromHeader(String rawToken) {
        return rawToken.replaceFirst(PREFIX, "");
    }
}
