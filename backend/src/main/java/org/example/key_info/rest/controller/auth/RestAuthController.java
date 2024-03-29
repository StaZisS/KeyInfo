package org.example.key_info.rest.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.auth.service.AuthService;
import org.example.key_info.public_interface.auth.JwtResponseDto;
import org.example.key_info.public_interface.auth.LoginDto;
import org.example.key_info.public_interface.client.ClientCreateDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Авторизация и регистрация")
public class RestAuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponseDto login(@RequestBody LoginDto dto) {
        return authService.login(dto);
    }

    @PostMapping("/register")
    public JwtResponseDto register(@RequestBody RegisterDto dto) {
        final ClientCreateDto clientCreateDto = RequestMapper.mapRequestToDto(dto);
        return authService.register(clientCreateDto);
    }

    @Operation(summary = "Получить новый access token по refresh token")
    @PostMapping("/token")
    public JwtResponseDto getAccessToken(@RequestBody RefreshJwtRequest dto) {
        return authService.getAccessToken(dto.refreshToken());
    }

    @Operation(summary = "Обновить refresh token")
    @PostMapping("/refresh")
    public JwtResponseDto refresh(@RequestBody RefreshJwtRequest dto) {
        return authService.refresh(dto.refreshToken());
    }

    @PostMapping("/logout")
    public void logout(@RequestBody RefreshJwtRequest dto) {
        authService.logout(dto.refreshToken());
    }
}
