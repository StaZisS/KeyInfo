package org.example.key_info.rest.controller.schedule;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
@Tag(name = "Для работы с расписанием")
public class RestScheduleController {

    @Operation(summary = "Получить свободные временные слоты")
    @GetMapping()
    public ResponseEntity<List<DayTimeSlots>> getFreeTimeSlots(@RequestParam(required = false, name = "start_time") OffsetDateTime startTime,
                                                               @RequestParam(required = false, name = "end_time") OffsetDateTime endTime,
                                                               @RequestParam(required = false, name = "build_id") int buildId,
                                                               @RequestParam(required = false, name = "room_id") int roomId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
