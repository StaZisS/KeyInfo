package org.example.key_info.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.key_info.core.accommodation.AccommodationService;
import org.example.key_info.rest.controller.deanery.StudyRoomDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/audiences")
@Tag(name = "Аудитории", description = "Работа с аудиториями")
public class AudienceController {
    private final AccommodationService accommodationService;

    @Operation(summary = "Получить все аудитории")
    @GetMapping("/accommodations")
    public ResponseEntity<List<StudyRoomDto>> getAllAccommodations() {
        return ResponseEntity.ok(accommodationService.getAllAccommodations());
    }

    @Operation(summary = "Получить все корпуса")
    @GetMapping("/accommodations/buildings")
    public ResponseEntity<List<Integer>> getAllBuildings() {
        return ResponseEntity.ok(accommodationService.getBuildings());
    }

    @Operation(summary = "Получить все комнаты в корпусе")
    @GetMapping("/accommodations/{build_id}/rooms")
    public ResponseEntity<List<Integer>> getAllRooms(@PathVariable(name = "build_id") Integer buildId) {
        return ResponseEntity.ok(accommodationService.getRooms(buildId));
    }
}