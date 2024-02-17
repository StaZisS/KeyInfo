package org.example.key_info.rest.util;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.auth.JwtAuthentication;
import org.example.key_info.core.auth.Role;
import org.example.key_info.core.auth.provider.JwtProvider;
import org.example.key_info.core.client.repository.ClientRole;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTools {

    public TokenInfo getClientInfoFromAccessToken(Authentication claims) {
        var jwtAuth = (JwtAuthentication) claims;
        var clientId = jwtAuth.getUserId();
        final Set<Role> stringRoles = jwtAuth.getRoles();
        var clientRoles = stringRoles.stream()
                .map(r -> ClientRole.getClientRoleByName(r.name()))
                .collect(Collectors.toSet());

        return new TokenInfo(clientId, clientRoles);
    }
}
