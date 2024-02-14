package org.example.key_info.core.key.service;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.core.key.repository.KeyEntity;
import org.example.key_info.core.key.repository.KeyRepository;
import org.example.key_info.core.key.repository.KeyStatus;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.example.key_info.public_interface.key.KeyCreateDto;
import org.example.key_info.public_interface.key.KeyDeleteDto;
import org.example.key_info.public_interface.key.KeyDto;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeyService {
    private static final List<ClientRole> ROLES_CONTROL_KEY_LIFECYCLE = List.of(ClientRole.DEANERY, ClientRole.ADMIN);

    private final KeyRepository keyRepository;

    public List<KeyDto> getMyKeys(UUID clientId) {
        return keyRepository.getMyKeys(clientId)
                .parallelStream()
                .map(this::mapEntityToDto)
                .toList();
    }

    public UUID createKey(KeyCreateDto dto) {
        var roles = dto.clientRoles();
        var isClientCanCreate = isClientCanCreate(roles);
        if (!isClientCanCreate) {
            throw new ExceptionInApplication(String.format("Пользователь с этими правами %s не может создавать ключи", roles), ExceptionType.INVALID);
        }

        var keyEntity = new KeyEntity(
                null,
                KeyStatus.IN_DEANERY,
                null,
                dto.roomId(),
                dto.buildId(),
                OffsetDateTime.now()
        );

        return keyRepository.createKey(keyEntity);
    }

    public void deleteKey(KeyDeleteDto dto) {
        var roles = dto.clientRoles();
        var isClientCanCreate = isClientCanCreate(roles);
        if (!isClientCanCreate) {
            throw new ExceptionInApplication(String.format("Пользователь с этими правами %s не может удалить ключ", roles), ExceptionType.INVALID);
        }

        keyRepository.deleteKey(dto.keyId());
    }

    private KeyDto mapEntityToDto(KeyEntity entity) {
        return new KeyDto(
                entity.keyId(),
                entity.status(),
                entity.keyHolderId(),
                entity.roomId(),
                entity.buildId(),
                entity.lastAccess()
        );
    }

    private boolean isClientCanCreate(Set<ClientRole> clientRoles) {
        return clientRoles
                .parallelStream()
                .anyMatch(ROLES_CONTROL_KEY_LIFECYCLE::contains);
    }
}
