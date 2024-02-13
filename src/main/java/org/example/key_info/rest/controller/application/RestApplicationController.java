package org.example.key_info.rest.controller.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/requests")
public class RestApplicationController {

    @GetMapping()
    public ResponseEntity<List<ApplicationResponseDto>> getMyApplications(@RequestHeader("Authorization") String accessToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PostMapping()
    public ResponseEntity<ApplicationResponseDto> createApplication(@RequestHeader("Authorization") String accessToken,
                                                                    @RequestBody CreateApplicationDto dto,
                                                                    @RequestParam(required = false) boolean duplicate) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyApplication(@RequestHeader("Authorization") String accessToken,
                                                    @PathVariable UUID id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto> updateApplication(@RequestHeader("Authorization") String accessToken,
                                                                    @PathVariable UUID id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
