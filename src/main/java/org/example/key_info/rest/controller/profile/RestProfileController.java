package org.example.key_info.rest.controller.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.service.ClientService;
import org.example.key_info.core.profile.ProfileService;
import org.example.key_info.core.profile.UpdateProfileDto;
import org.example.key_info.public_interface.client.ClientProfileDto;
import org.example.key_info.public_interface.profile.ProfileDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ClientProfileDto> getMyProfile() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var profileDto = clientService.getProfile(infoAboutClient.clientId());

        return ResponseEntity.ok(profileDto);
    }

    @Operation(summary = "Обновить свой профиль")
    @PutMapping()
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ProfileDto> updateMyProfile(@RequestBody ProfileDto dto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var updateProfileDto = new UpdateProfileDto(
                infoAboutClient.clientId(),
                dto.name(),
                dto.email(),
                dto.password()
        );

        return ResponseEntity.ok(profileService.updateProfile(updateProfileDto));
    }
}
