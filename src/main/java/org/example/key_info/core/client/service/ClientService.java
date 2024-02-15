package org.example.key_info.core.client.service;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.repository.ClientEntity;
import org.example.key_info.core.client.repository.ClientRepository;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.core.client.validation.ClientValidationService;
import org.example.key_info.core.util.PasswordTool;
import org.example.key_info.public_interface.client.ClientCreateDto;
import org.example.key_info.public_interface.client.ClientProfileDto;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientValidationService clientValidationService;

    public UUID createClient(ClientCreateDto dto) {
        clientValidationService.validateCreateClient(dto);

        var entity = formatClientCreateEntity(dto);

        return clientRepository.createClient(entity)
                .orElseThrow(() -> new ExceptionInApplication("Ошибка при создании клиента", ExceptionType.FATAL));
    }

    public Optional<ClientEntity> getByEmail(String email) {
        return clientRepository.getClientByEmail(email);
    }

    public Optional<ClientEntity> getByClientId(UUID clientId) {
        return clientRepository.getClientByClientId(clientId);
    }

    public ClientProfileDto getProfile(UUID clientId) {
        var clientEntity = clientRepository.getClientByClientId(clientId)
                .orElseThrow(() -> new ExceptionInApplication("Клиент не найден", ExceptionType.NOT_FOUND));

        return formatToDto(clientEntity);
    }

    private ClientEntity formatClientCreateEntity(ClientCreateDto dto) {
        return new ClientEntity(
                null,
                dto.name(),
                dto.email(),
                PasswordTool.getHashPassword(dto.password()),
                dto.gender(),
                OffsetDateTime.now(),
                Set.of(ClientRole.UNSPECIFIED)
        );
    }

    private ClientProfileDto formatToDto(ClientEntity entity) {
        return new ClientProfileDto(
                entity.clientId(),
                entity.name(),
                entity.email(),
                entity.gender(),
                entity.createdDate(),
                entity.role()
        );
    }
}
