package org.example.key_info.rest.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.accommodation.AccommodationService;
import org.example.key_info.public_interface.accommodation.AccommodationCreateDto;
import org.example.key_info.public_interface.accommodation.AccommodationDeleteDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
@Tag(name = "Админские темки")
public class RestAdminController {
    private final JwtTools jwtTools;
    private final AccommodationService accommodationService;

    @Operation(summary = "Добавляет новую аудиторию в здание")
    @PostMapping("/accommodations/{build_id}/{room_id}")
    public ResponseEntity<Void> createAccommodation(@RequestHeader("Authorization") String accessToken,
                                                    @PathVariable(name = "build_id") int buildId,
                                                    @PathVariable(name = "room_id") int roomId) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var accommodationCreateDto = new AccommodationCreateDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                buildId,
                roomId
        );
        accommodationService.createAccommodation(accommodationCreateDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаляет аудиторию из здания")
    @DeleteMapping("/accommodations/{build_id}/{room_id}")
    public ResponseEntity<Void> deleteAccommodation(@RequestHeader("Authorization") String accessToken,
                                                    @PathVariable(name = "build_id") int buildId,
                                                    @PathVariable(name = "room_id") int roomId) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var accommodationDeleteDto = new AccommodationDeleteDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                buildId,
                roomId
        );
        accommodationService.deleteAccommodation(accommodationDeleteDto);

        return ResponseEntity.ok().build();
    }
}
