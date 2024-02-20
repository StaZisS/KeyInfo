package org.example.key_info.core.auth.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.auth.Role;
import org.example.key_info.core.auth.provider.DataForGenerateToken;
import org.example.key_info.core.client.repository.ClientEntity;
import org.example.key_info.core.client.service.ClientService;
import org.example.key_info.core.util.PasswordTool;
import org.example.key_info.public_interface.auth.JwtResponseDto;
import org.example.key_info.public_interface.auth.LoginDto;
import org.example.key_info.public_interface.client.ClientCreateDto;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ClientService clientService;
    private final TokenService tokenService;

    public JwtResponseDto login(@NonNull LoginDto dto) {
        final ClientEntity client = clientService.getByEmail(dto.email())
                .orElseThrow(() -> new ExceptionInApplication("Неверная почта или пароль", ExceptionType.NOT_FOUND));

        if (PasswordTool.isCorrectPassword(client.password(), dto.password())) {
            var dataForGenerateToken = forGenerateToken(client);
            return tokenService.getTokens(dataForGenerateToken);
        }

        throw new ExceptionInApplication("Неверная почта или пароль", ExceptionType.INVALID);
    }

    public JwtResponseDto register(@NonNull ClientCreateDto dto) {
        clientService.createClient(dto);
        var loginDto = new LoginDto(dto.email(), dto.password());

        return login(loginDto);
    }

    public JwtResponseDto getAccessToken(@NonNull String refreshToken) {
        tokenService.checkRefreshToken(refreshToken);

        final String clientId = tokenService.getClientIdInRefreshToken(refreshToken);

        final ClientEntity client = clientService.getByClientId(tryParseUUID(clientId))
                .orElseThrow(() -> new ExceptionInApplication("Клиент не найден", ExceptionType.NOT_FOUND));
        var dataForGenerateToken = forGenerateToken(client);

        return tokenService.getAccessToken(dataForGenerateToken);
    }

    public JwtResponseDto refresh(@NonNull String refreshToken) {
        tokenService.checkRefreshToken(refreshToken);

        final String tokenId = tokenService.getTokenIdInRefreshToken(refreshToken);
        final String clientId = tokenService.getClientIdInRefreshToken(refreshToken);

        tokenService.deleteRefreshToken(tokenId);

        final ClientEntity client = clientService.getByClientId(tryParseUUID(clientId))
                .orElseThrow(() -> new ExceptionInApplication("Клиент не найден", ExceptionType.NOT_FOUND));
        var dataForGenerateToken = forGenerateToken(client);

        return tokenService.getTokens(dataForGenerateToken);
    }

    public void logout(@NonNull String refreshToken) {
        tokenService.checkRefreshToken(refreshToken);

        final String tokenId = tokenService.getTokenIdInRefreshToken(refreshToken);

        tokenService.deleteRefreshToken(tokenId);
    }

    private DataForGenerateToken forGenerateToken(ClientEntity entity) {
        var mappedRoles = entity.role().stream()
                .map(r -> Role.getRoleByName(r.name()))
                .collect(Collectors.toSet());
        return new DataForGenerateToken(entity.clientId().toString(), mappedRoles);
    }

    private UUID tryParseUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            throw new ExceptionInApplication("Не удалось распарсить ClientId", ExceptionType.INVALID);
        }
    }
}
