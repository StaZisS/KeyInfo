package org.example.key_info.rest.controller.deanery;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.application.ApplicationFilterDto;
import org.example.key_info.core.application.ApplicationService;
import org.example.key_info.core.application.ApplicationStatus;
import org.example.key_info.core.key.service.KeyService;
import org.example.key_info.public_interface.application.AcceptApplicationDto;
import org.example.key_info.public_interface.application.ApplicationDto;
import org.example.key_info.public_interface.application.DeclineApplicationDto;
import org.example.key_info.public_interface.application.GetAllApplicationsDto;
import org.example.key_info.public_interface.deaneries.AcceptKeyDeaneriesDto;
import org.example.key_info.public_interface.deaneries.GiveKeyDeaneriesDto;
import org.example.key_info.public_interface.key.GetKeysDto;
import org.example.key_info.public_interface.key.KeyCreateDto;
import org.example.key_info.public_interface.key.KeyDeleteDto;
import org.example.key_info.public_interface.key.KeyDto;
import org.example.key_info.rest.controller.application.ApplicationResponseDto;
import org.example.key_info.rest.controller.key.ResponseKeyDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deaneries")
public class RestDeaneriesController {
    private final KeyService keyService;
    private final ApplicationService applicationService;
    private final JwtTools jwtTools;

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponseDto>> getPossibleApplications(@RequestHeader("Authorization") String accessToken,
                                                                                @RequestParam(required = false, defaultValue = "IN_PROCESS") String status,
                                                                                @RequestParam(required = false) OffsetDateTime start,
                                                                                @RequestParam(required = false) OffsetDateTime end,
                                                                                @RequestParam(required = false, name = "build_id") Integer buildId,
                                                                                @RequestParam(required = false, name = "room_id") Integer roomId) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

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

    @PostMapping("/application/{id}/accept")
    public ResponseEntity<Void> acceptApplication(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable(name = "id") UUID applicationId) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var acceptApplicationDto = new AcceptApplicationDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                applicationId
        );
        applicationService.acceptApplication(acceptApplicationDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/application/{id}/decline")
    public ResponseEntity<Void> declineApplication(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable(name = "id") UUID applicationId) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var declineApplicationDto = new DeclineApplicationDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                applicationId
        );
        applicationService.declineApplication(declineApplicationDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/keys")
    public ResponseEntity<List<ResponseKeyDto>> getAllKeys(@RequestHeader("Authorization") String accessToken,
                                                           @RequestParam(required = false, name = "key_status") String keyStatus,
                                                           @RequestParam(required = false) Integer build,
                                                           @RequestParam(required = false) Integer room) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);
        var getKeysDto = new GetKeysDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                keyStatus,
                build,
                room
        );

        var keys = keyService.getAllKeys(getKeysDto);
        var body = keys.parallelStream()
                .map(this::mapToResponseDto)
                .toList();

        return ResponseEntity.ok(body);
    }

    @PostMapping("/keys")
    public ResponseEntity<UUID> addKey(@RequestHeader("Authorization") String accessToken,
                                                 @RequestBody AddKeyDto dto) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var keyCreateDto = new KeyCreateDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                dto.room(),
                dto.build()
        );

        var keyId = keyService.createKey(keyCreateDto);

        return ResponseEntity.ok(keyId);
    }

    @DeleteMapping("/keys/{id}")
    public ResponseEntity<Void> deleteKey(@RequestHeader("Authorization") String accessToken,
                                          @PathVariable(name = "id") UUID keyId) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var keyDeleteDto = new KeyDeleteDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                keyId
        );

        keyService.deleteKey(keyDeleteDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/keys/giving/{id}")
    public ResponseEntity<Void> giveKey(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable(name = "id") UUID keyId,
                                                  @RequestParam(required = false) UUID keyHolderId) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var giveKeyDeaneriesDto = new GiveKeyDeaneriesDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                keyHolderId,
                keyId
        );
        keyService.giveKeyDeaneries(giveKeyDeaneriesDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/keys/acceptance/{id}")
    public ResponseEntity<Void> acceptKey(@RequestHeader("Authorization") String accessToken,
                                                    @PathVariable(name = "id") UUID keyId,
                                                    @RequestParam(required = false) UUID keyHolderId) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var acceptKeyDeaneriesDto = new AcceptKeyDeaneriesDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                keyHolderId,
                keyId
        );
        keyService.acceptKeyDeaneries(acceptKeyDeaneriesDto);

        return ResponseEntity.ok().build();
    }

    private ResponseKeyDto mapToResponseDto(KeyDto dto) {
        return new org.example.key_info.rest.controller.key.ResponseKeyDto(
                dto.keyId(),
                dto.buildId(),
                dto.roomId(),
                dto.lastAccess()
        );
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
                dto.endTimeToDuplicate()
        );
    }
}
