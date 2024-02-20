package org.example.key_info.rest.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.user.GetUsersDto;
import org.example.key_info.core.user.UserService;
import org.example.key_info.public_interface.client.ClientProfileDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Для работы с пользователями")
public class RestUserController {
    private final UserService userService;
    private final JwtTools jwtTools;

    @Operation(summary = "Получить всех пользователей")
    @GetMapping()
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ClientProfileDto>> getAllUsers(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) String email) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var getUsersDto = new GetUsersDto(infoAboutClient.clientRoles(), name, email);

        return ResponseEntity.ok(userService.getAllUsers(getUsersDto));
    }
}
