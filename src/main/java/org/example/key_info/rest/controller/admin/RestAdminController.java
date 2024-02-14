package org.example.key_info.rest.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.accommodation.AccommodationService;
import org.example.key_info.public_interface.accommodation.AccommodationCreateDto;
import org.example.key_info.public_interface.accommodation.AccommodationDeleteDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class RestAdminController {
    private final JwtTools jwtTools;
    private final AccommodationService accommodationService;

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