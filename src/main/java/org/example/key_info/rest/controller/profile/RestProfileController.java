package org.example.key_info.rest.controller.profile;

import lombok.RequiredArgsConstructor;
import org.example.key_info.public_interface.profile.ProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class RestProfileController {

    @GetMapping()
    public ResponseEntity<ProfileDto> getMyProfile(@RequestHeader("Authorization") String accessToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PutMapping()
    public ResponseEntity<ProfileDto> updateMyProfile(@RequestHeader("Authorization") String accessToken,
                                                      @RequestBody ProfileDto dto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
