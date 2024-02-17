package org.example.key_info.rest.controller.schedule;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.public_interface.schedule.FreeAudienceDto;
import org.example.key_info.public_interface.schedule.GetFreeAudienceDto;
import org.example.key_info.public_interface.schedule.GetFreeTimeSlotsDto;
import org.example.key_info.core.schedule.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
@Tag(name = "Для работы с расписанием")
public class RestScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "Получить свободные временные слоты")
    @GetMapping("/slot")
    public ResponseEntity<List<DayTimeSlots>> getFreeTimeSlots(@RequestParam(required = false, name = "start_time") OffsetDateTime startTime,
                                                               @RequestParam(required = false, name = "end_time") OffsetDateTime endTime,
                                                               @RequestParam(required = false, name = "build_id") Integer buildId,
                                                               @RequestParam(required = false, name = "room_id") Integer roomId) {
        var getFreeTimeSlotsDto = new GetFreeTimeSlotsDto(startTime, endTime, buildId, roomId);

        var freeTimeSlots = scheduleService.getFreeTimeSlots(getFreeTimeSlotsDto);

        return ResponseEntity.ok(freeTimeSlots);
    }

    @Operation(summary = "Получить свободные аудитории, обязательно должно быть указано время одной пары")
    @GetMapping("/audience")
    public ResponseEntity<List<FreeAudienceResponseDto>> getFreeAudience(@RequestParam(name = "start_time") OffsetDateTime startTime,
                                                                         @RequestParam(name = "end_time") OffsetDateTime endTime,
                                                                         @RequestParam(required = false, name = "build_id") Integer buildId,
                                                                         @RequestParam(required = false, name = "room_id") Integer roomId) {
        var getFreeAudienceDto = new GetFreeAudienceDto(startTime, endTime, buildId, roomId);
        var audience = scheduleService.getFreeAudience(getFreeAudienceDto);

        var body = audience.stream()
                .map(this::mapToResponseDto)
                .toList();

        return ResponseEntity.ok(body);
    }

    private FreeAudienceResponseDto mapToResponseDto(FreeAudienceDto dto) {
        return new FreeAudienceResponseDto(
                dto.buildId(),
                dto.roomId(),
                dto.startTime(),
                dto.endTime()
        );
    }
}
