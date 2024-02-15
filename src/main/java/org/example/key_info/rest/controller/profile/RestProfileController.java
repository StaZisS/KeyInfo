package org.example.key_info.rest.controller.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.service.ClientService;
import org.example.key_info.core.profile.ProfileService;
import org.example.key_info.core.profile.UpdateProfileDto;
import org.example.key_info.public_interface.client.ClientProfileDto;
import org.example.key_info.public_interface.profile.ProfileDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
@Tag(name = "Для работы с профилем пользователя")
public class RestProfileController {
    private final ClientService clientService;
    private final ProfileService profileService;
    private final JwtTools jwtTools;

    @Operation(summary = "Получить свой профиль")
    @GetMapping()
    public ResponseEntity<ClientProfileDto> getMyProfile(@RequestHeader("Authorization") String accessToken) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var profileDto = clientService.getProfile(infoAboutClient.clientId());

        return ResponseEntity.ok(profileDto);
    }

    @Operation(summary = "Обновить свой профиль")
    @PutMapping()
    public ResponseEntity<ProfileDto> updateMyProfile(@RequestHeader("Authorization") String accessToken,
                                                      @RequestBody ProfileDto dto) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        var updateProfileDto = new UpdateProfileDto(
                infoAboutClient.clientId(),
                dto.name(),
                dto.email(),
                dto.password()
        );

        return ResponseEntity.ok(profileService.updateProfile(updateProfileDto));
    }
}
