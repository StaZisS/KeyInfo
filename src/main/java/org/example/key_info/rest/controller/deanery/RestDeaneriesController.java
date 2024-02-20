package org.example.key_info.rest.controller.deanery;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.accommodation.AccommodationService;
import org.example.key_info.core.application.ApplicationFilterDto;
import org.example.key_info.core.application.ApplicationService;
import org.example.key_info.core.application.ApplicationStatus;
import org.example.key_info.core.key.service.KeyService;
import org.example.key_info.public_interface.accommodation.AccommodationCreateDto;
import org.example.key_info.public_interface.accommodation.AccommodationDeleteDto;
import org.example.key_info.public_interface.application.AcceptApplicationDto;
import org.example.key_info.public_interface.application.ApplicationDto;
import org.example.key_info.public_interface.application.DeclineApplicationDto;
import org.example.key_info.public_interface.application.GetAllApplicationsDto;
import org.example.key_info.public_interface.deaneries.AcceptKeyDeaneriesDto;
import org.example.key_info.public_interface.deaneries.GiveKeyDeaneriesDto;
import org.example.key_info.public_interface.key.ChangePrivateStatusKeyDto;
import org.example.key_info.public_interface.key.GetKeysDto;
import org.example.key_info.public_interface.key.KeyCreateDto;
import org.example.key_info.public_interface.key.KeyDeleteDto;
import org.example.key_info.rest.controller.application.ApplicationResponseDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deaneries")
@Tag(name = "Работа деканата")
public class RestDeaneriesController {
    private final AccommodationService accommodationService;
    private final ApplicationService applicationService;
    private final KeyService keyService;
    private final JwtTools jwtTools;

    @Operation(summary = "Деканат добавляет новую аудиторию в здание")
    @PostMapping("/accommodations/{build_id}/{room_id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> createAccommodation(@PathVariable(name = "build_id") int buildId,
                                                    @PathVariable(name = "room_id") int roomId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var accommodationCreateDto = new AccommodationCreateDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                buildId,
                roomId
        );
        accommodationService.createAccommodation(accommodationCreateDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Деканат удаляет аудиторию из здания")
    @DeleteMapping("/accommodations/{build_id}/{room_id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteAccommodation(@PathVariable(name = "build_id") int buildId,
                                                    @PathVariable(name = "room_id") int roomId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var accommodationDeleteDto = new AccommodationDeleteDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                buildId,
                roomId
        );
        accommodationService.deleteAccommodation(accommodationDeleteDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить доступные заявки для рассмотрения")
    @GetMapping("/applications")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ApplicationResponseDto>> getPossibleApplications(@RequestParam(required = false, defaultValue = "IN_PROCESS") String status,
                                                                                @RequestParam(required = false) OffsetDateTime start,
                                                                                @RequestParam(required = false) OffsetDateTime end,
                                                                                @RequestParam(required = false, name = "build_id") Integer buildId,
                                                                                @RequestParam(required = false, name = "room_id") Integer roomId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var filterDto = new ApplicationFilterDto(
                ApplicationStatus.getApplicationStatusByName(status),
                start,
                end,
                buildId,
                roomId
        );
        var getAllApplicationDto = new GetAllApplicationsDto(
                filterDto,
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles()
        );
        var applications = applicationService.getAllApplications(getAllApplicationDto);

        var body = applications.parallelStream()
                .map(this::mapToResponseDto)
                .toList();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Принять заявку")
    @PostMapping("/application/{id}/accept")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> acceptApplication(@PathVariable(name = "id") UUID applicationId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var acceptApplicationDto = new AcceptApplicationDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                applicationId
        );
        applicationService.acceptApplication(acceptApplicationDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Отклонить заявку")
    @PostMapping("/application/{id}/decline")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> declineApplication(@PathVariable(name = "id") UUID applicationId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var declineApplicationDto = new DeclineApplicationDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                applicationId
        );
        applicationService.declineApplication(declineApplicationDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить все ключи (см. параметры)")
    @GetMapping("/keys")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ResponseKeyDto>> getAllKeys(@RequestParam(required = false, name = "key_status") String keyStatus,
                                                           @RequestParam(required = false) Integer build,
                                                           @RequestParam(required = false) Integer room,
                                                           @RequestParam(required = false, name = "is_private") Boolean isPrivate) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);
        var getKeysDto = new GetKeysDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                keyStatus,
                build,
                room,
                isPrivate
        );

        var keys = keyService.getAllKeys(getKeysDto);

        return ResponseEntity.ok(keys);
    }

    @Operation(summary = "Добавить новый ключ")
    @PostMapping("/keys")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UUID> addKey(@RequestBody AddKeyDto dto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var keyCreateDto = new KeyCreateDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                dto.room(),
                dto.build(),
                dto.isPrivate()
        );

        var keyId = keyService.createKey(keyCreateDto);

        return ResponseEntity.ok(keyId);
    }

    @Operation(summary = "Удалить ключ")
    @DeleteMapping("/keys/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteKey(@PathVariable(name = "id") UUID keyId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var keyDeleteDto = new KeyDeleteDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                keyId
        );

        keyService.deleteKey(keyDeleteDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Изменить приватность ключа")
    @PatchMapping("/keys/replacing")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> changePrivateKey(@RequestBody ChangePrivateKeyDto dto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var changePrivateStatusKeyDto = new ChangePrivateStatusKeyDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                dto.keyId(),
                dto.isPrivate()
        );

        keyService.changePrivateKey(changePrivateStatusKeyDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Передать ключ пользователю")
    @PatchMapping("/keys/giving/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> giveKey(@PathVariable(name = "id") UUID keyId,
                                        @RequestParam(required = false) UUID keyHolderId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var giveKeyDeaneriesDto = new GiveKeyDeaneriesDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                keyHolderId,
                keyId
        );
        keyService.giveKeyDeaneries(giveKeyDeaneriesDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Принять ключ от пользователя")
    @PatchMapping("/keys/acceptance/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> acceptKey(@PathVariable(name = "id") UUID keyId,
                                          @RequestParam(required = false) UUID keyHolderId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var acceptKeyDeaneriesDto = new AcceptKeyDeaneriesDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                keyHolderId,
                keyId
        );
        keyService.acceptKeyDeaneries(acceptKeyDeaneriesDto);

        return ResponseEntity.ok().build();
    }

    private ApplicationResponseDto mapToResponseDto(ApplicationDto dto) {
        return new ApplicationResponseDto(
                dto.applicationId(),
                dto.applicationCreatorId(),
                dto.clientName(),
                dto.clientEmail(),
                dto.startTime(),
                dto.endTime(),
                dto.status().name(),
                dto.isDuplicate(),
                dto.endTimeToDuplicate(),
                dto.buildId(),
                dto.roomId()
        );
    }
}
