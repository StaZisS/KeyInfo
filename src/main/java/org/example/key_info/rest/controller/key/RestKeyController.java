package org.example.key_info.rest.controller.key;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.key.service.KeyService;
import org.example.key_info.public_interface.key.KeyDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/keys")
@Tag(name = "Для работы с ключами пользователя")
public class RestKeyController {
    private final KeyService keyService;
    private final JwtTools jwtTools;

    @Operation(summary = "Получить все ключи пользователя (на руках)")
    @GetMapping()
    public ResponseEntity<List<ResponseKeyDto>> getMyKeys(@RequestHeader("Authorization") String accessToken) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var body = keyService.getMyKeys(infoAboutClient.clientId())
                .parallelStream()
                .map(this::mapToResponseDto)
                .toList();

        return ResponseEntity.ok(body);
    }

    private ResponseKeyDto mapToResponseDto(KeyDto dto) {
        return new ResponseKeyDto(
                dto.keyId(),
                dto.buildId(),
                dto.roomId(),
                dto.lastAccess()
        );
    }
}
