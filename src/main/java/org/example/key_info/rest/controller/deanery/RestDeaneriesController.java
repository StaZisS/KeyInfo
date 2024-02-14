package org.example.key_info.rest.controller.deanery;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.key.service.KeyService;
import org.example.key_info.public_interface.key.KeyCreateDto;
import org.example.key_info.public_interface.key.KeyDeleteDto;
import org.example.key_info.rest.controller.application.ApplicationResponseDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deaneries")
public class RestDeaneriesController {
    private final KeyService keyService;
    private final JwtTools jwtTools;

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponseDto>> getPossibleApplications(@RequestHeader("Authorization") String accessToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping("/keys")
    public ResponseEntity<List<ResponseKeyDto>> getAllKeys(@RequestHeader("Authorization") String accessToken,
                                                           @RequestParam(required = false) String keyId,
                                                           @RequestParam(required = false) String keyStatus,
                                                           @RequestParam(required = false) int build,
                                                           @RequestParam(required = false) int room) {
        throw new UnsupportedOperationException("Not implemented yet");
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
    public ResponseEntity<ResponseKeyDto> giveKey(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable(name = "id") UUID keyId,
                                                  @RequestParam(required = false) UUID keyHolderId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PatchMapping("/keys/acceptance/{id}")
    public ResponseEntity<ResponseKeyDto> acceptKey(@RequestHeader("Authorization") String accessToken,
                                                    @PathVariable(name = "id") UUID keyId,
                                                    @RequestParam(required = false) UUID keyHolderId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
