package org.example.key_info.rest.controller.role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.auth.Role;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.core.role.RoleService;
import org.example.key_info.core.role.UserStatusUpdateDto;
import org.example.key_info.public_interface.role.RoleDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
@Tag(name = "Для работы с ролями пользователя")
public class RestRoleController {
    private final JwtTools jwtTools;
    private final RoleService roleService;

    @Operation(summary = "Получить все роли пользователя (их несколько может быть)")
    @GetMapping()
    public ResponseEntity<RoleDto> getMyRoles(@RequestHeader("Authorization") String accessToken) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);

        Set<String> roles = infoAboutClient.clientRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new RoleDto(roles));
    }

    @Operation(summary = "Администратор назначает деканату роль, деканат назначает студенту/учителю роль")
    @PatchMapping("/{user_id}/status")
    public ResponseEntity<Void> updateUserStatus(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable(name = "user_id") UUID userId,
                                                 @RequestBody UpdateClientRoles role) {
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(accessToken);


        var userStatusUpdateDto = new UserStatusUpdateDto(
                infoAboutClient.clientId(),
                infoAboutClient.clientRoles(),
                userId,
                role.clientRoles()
        );

        roleService.assignRoleToUser(userStatusUpdateDto);
        return ResponseEntity.ok().build();
    }
}
