package org.example.key_info.rest.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class RestAdminController {

    @PostMapping("/accommodations/{build_id}/{room_id}")
    public ResponseEntity<Void> createAccommodation(@RequestHeader("Authorization") String accessToken,
                                                    @PathVariable(name = "build_id") UUID buildId,
                                                    @PathVariable(name = "room_id") UUID roomId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @DeleteMapping("/accommodations/{build_id}/{room_id}")
    public ResponseEntity<Void> deleteAccommodation(@RequestHeader("Authorization") String accessToken,
                                                    @PathVariable(name = "build_id") UUID buildId,
                                                    @PathVariable(name = "room_id") UUID roomId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
