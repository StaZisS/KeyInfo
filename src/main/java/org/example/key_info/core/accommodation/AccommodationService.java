package org.example.key_info.core.accommodation;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.core.key.repository.KeyRepository;
import org.example.key_info.public_interface.accommodation.AccommodationCreateDto;
import org.example.key_info.public_interface.accommodation.AccommodationDeleteDto;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.example.key_info.rest.controller.deanery.StudyRoomDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private static final List<ClientRole> ROLES_CONTROL_ACCOMMODATION_LIFECYCLE = List.of(ClientRole.DEANERY);
    private final KeyRepository keyRepository;
    private final AccommodationRepository accommodationRepository;

    public void createAccommodation(AccommodationCreateDto dto) {
        validateClientRoles(dto.clientRoles());

        var accommodationEntity = new AccommodationEntity(dto.buildId(), dto.roomId());

        accommodationRepository.createAccommodation(accommodationEntity);
    }

    public void deleteAccommodation(AccommodationDeleteDto dto) {
        validateClientRoles(dto.clientRoles());

        checkExistenceKey(dto.buildId(), dto.roomId());

        checkExistenceAccommodation(dto.buildId(), dto.roomId());

        accommodationRepository.deleteAccommodation(dto.buildId(), dto.roomId());
    }

    public List<StudyRoomDto> getAllAccommodations() {
        var accommodations = accommodationRepository.getAllAccommodations();

        return accommodations.stream()
                .map(accommodationEntity ->
                        new StudyRoomDto(accommodationEntity.buildId(), accommodationEntity.roomId()))
                .toList();
    }

    public List<Integer> getBuildings() {
        return accommodationRepository.getBuildings();
    }

    public List<Integer> getRooms(int buildId) {
        return accommodationRepository.getRooms(buildId);
    }

    private void validateClientRoles(Set<ClientRole> roles) {
        if (!isClientHaveRole(roles)) {
            throw new ExceptionInApplication(String.format("Пользователь с правами %s не может управлять аудиторями", roles), ExceptionType.INVALID);
        }
    }

    private boolean isClientHaveRole(Set<ClientRole> clientRoles) {
        return clientRoles
                .stream()
                .anyMatch(ROLES_CONTROL_ACCOMMODATION_LIFECYCLE::contains);
    }

    private void checkExistenceKey(int buildId, int roomId) {
        var keys = keyRepository.getAllKeys(buildId, roomId, null);

        if (!keys.isEmpty()) {
            throw new ExceptionInApplication("Нельзя удалить аудиторию, в которой есть ключи", ExceptionType.INVALID);
        }
    }

    private void checkExistenceAccommodation(int buildId, int roomId) {
        var accommodation = accommodationRepository.getAccommodation(buildId, roomId);

        if (accommodation.isEmpty()) {
            throw new ExceptionInApplication("Аудитория не найдена", ExceptionType.INVALID);
        }
    }
}
