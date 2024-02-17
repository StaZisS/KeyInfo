package org.example.key_info.rest.controller.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.application.ApplicationFilterDto;
import org.example.key_info.core.application.ApplicationService;
import org.example.key_info.core.application.ApplicationStatus;
import org.example.key_info.public_interface.application.ApplicationDto;
import org.example.key_info.public_interface.application.CreateApplicationDto;
import org.example.key_info.public_interface.application.DeleteApplicationDto;
import org.example.key_info.public_interface.application.GetMyApplicationDto;
import org.example.key_info.public_interface.application.UpdateApplicationDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/requests")
@Tag(name = "Заявки-запросы на бронь аудиторий")
public class RestApplicationController {
    private final ApplicationService applicationService;
    private final JwtTools jwtTools;

    @Operation(summary = "Получить свои заявки-запросы")
    @GetMapping()
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<ApplicationResponseDto>> getMyApplications(@RequestParam(required = false, defaultValue = "IN_PROCESS") String status,
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
        var getMyApplicationDto = new GetMyApplicationDto(
                filterDto,
                infoAboutClient.clientId()
        );
        var applications = applicationService.getMyApplications(getMyApplicationDto);

        var body = applications.stream()
                .map(this::mapToResponseDto)
                .toList();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Создать заявку-запрос")
    @PostMapping()
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UUID> createApplication(@RequestBody CreateApplicationDtoRequest dto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var createApplicationDto = new CreateApplicationDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                dto.startTime(),
                dto.endTime(),
                dto.buildId(),
                dto.roomId(),
                dto.role(),
                dto.isDuplicate(),
                dto.endTimeToDuplicate()
        );
        var applicationId = applicationService.createApplication(createApplicationDto);

        return ResponseEntity.ok(applicationId);
    }

    @Operation(summary = "Удалить мою заявку-запрос")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteMyApplication(@PathVariable UUID id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var deleteApplicationDto = new DeleteApplicationDto(
                infoAboutClient.clientId(),
                id
        );
        applicationService.deleteApplication(deleteApplicationDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновить мою заявку-запрос")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> updateApplication(@PathVariable UUID id,
                                                  @RequestBody UpdateApplicationDtoRequest dto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var updateApplicationDto = new UpdateApplicationDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                id,
                dto.startTime(),
                dto.endTime(),
                dto.buildId(),
                dto.roomId(),
                dto.role(),
                dto.isDuplicate(),
                dto.endTimeToDuplicate()
        );
        applicationService.updateApplication(updateApplicationDto);

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
                dto.endTimeToDuplicate()
        );
    }
}
