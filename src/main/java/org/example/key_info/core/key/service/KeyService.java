package org.example.key_info.core.key.service;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.accommodation.AccommodationRepository;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.core.key.repository.FilterKeyDto;
import org.example.key_info.core.key.repository.KeyEntity;
import org.example.key_info.core.key.repository.KeyRepository;
import org.example.key_info.core.key.repository.KeyStatus;
import org.example.key_info.public_interface.deaneries.AcceptKeyDeaneriesDto;
import org.example.key_info.public_interface.deaneries.GiveKeyDeaneriesDto;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.example.key_info.public_interface.key.GetKeysDto;
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
    private final AccommodationRepository accommodationRepository;
    private final KeyRepository keyRepository;

    public List<KeyDto> getAllKeys(GetKeysDto dto) {
        checkClientRoles(dto.roles());

        try {
        if (dto.keyStatus() == null) {
            return keyRepository.getAllKeys(dto.buildId(), dto.roomId())
                    .stream()
                    .map(this::mapEntityToDto)
                    .toList();
        } }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        var filterKeyDto = new FilterKeyDto(
                KeyStatus.getKeyStatusByName(dto.keyStatus()),
                dto.buildId(),
                dto.roomId()
        );

        return keyRepository.getAllKeys(filterKeyDto)
                .stream()
                .map(this::mapEntityToDto)
                .toList();
    }

    public List<KeyDto> getMyKeys(UUID clientId) {
        return keyRepository.getMyKeys(clientId)
                .parallelStream()
                .map(this::mapEntityToDto)
                .toList();
    }

    public UUID createKey(KeyCreateDto dto) {
        checkClientRoles(dto.clientRoles());

        checkAccommodationExists(dto);

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
        checkClientRoles(dto.clientRoles());

        keyRepository.deleteKey(dto.keyId());
    }

    public void giveKeyDeaneries(GiveKeyDeaneriesDto dto) {
        checkClientRoles(dto.clientRoles());

        var key = keyRepository.getKey(dto.keyId())
                .orElseThrow(() -> new ExceptionInApplication("Ключ не найден", ExceptionType.NOT_FOUND));

        var updatedKey = new KeyEntity(
                key.keyId(),
                KeyStatus.IN_HAND,
                dto.receiverId(),
                key.roomId(),
                key.buildId(),
                OffsetDateTime.now()
        );
        keyRepository.updateKey(updatedKey);
    }

    public void acceptKeyDeaneries(AcceptKeyDeaneriesDto dto) {
        checkClientRoles(dto.clientRoles());

        var key = keyRepository.getKey(dto.keyId())
                .orElseThrow(() -> new ExceptionInApplication("Ключ не найден", ExceptionType.NOT_FOUND));

        var updatedKey = new KeyEntity(
                key.keyId(),
                KeyStatus.IN_DEANERY,
                null,
                key.roomId(),
                key.buildId(),
                OffsetDateTime.now()
        );
        keyRepository.updateKey(updatedKey);
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

    private void checkClientRoles(Set<ClientRole> clientRoles) {
        if(!isClientHaveRoles(clientRoles)) {
            throw new ExceptionInApplication(String.format("Пользователь с правами %s не может выполнить это действие", clientRoles), ExceptionType.INVALID);
        }
    }

    private boolean isClientHaveRoles(Set<ClientRole> clientRoles) {
        return clientRoles
                .stream()
                .anyMatch(ROLES_CONTROL_KEY_LIFECYCLE::contains);
    }

    private void checkAccommodationExists(KeyCreateDto dto) {
        var accommodation = accommodationRepository.getAccommodation(dto.buildId(), dto.roomId());
        if (accommodation.isEmpty()) {
            throw new ExceptionInApplication(String.format("Аудитория %s в корпусе %s не найдена", dto.roomId(), dto.buildId()),
                    ExceptionType.NOT_FOUND);
        }
    }
}
