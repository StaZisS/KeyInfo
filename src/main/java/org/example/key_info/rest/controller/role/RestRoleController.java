package org.example.key_info.rest.controller.role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.public_interface.role.RoleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
@Tag(name = "Для работы с ролями пользователя")
public class RestRoleController {

    @Operation(summary = "Получить все роли пользователя (их несколько может быть)")
    @GetMapping()
    public ResponseEntity<RoleDto> getMyRoles(@RequestHeader("Authorization") String accessToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Operation(summary = "Администратор назначает деканату роль, деканат назначает студенту/учителю роль")
    @PatchMapping("/{user_id}/status")
    public ResponseEntity<Void> changeUserStatus(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable(name = "user_id") UUID userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
