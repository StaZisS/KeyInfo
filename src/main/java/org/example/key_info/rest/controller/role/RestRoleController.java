package org.example.key_info.rest.controller.role;

import lombok.RequiredArgsConstructor;
import org.example.key_info.public_interface.role.RoleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RestRoleController {

    @GetMapping()
    public ResponseEntity<RoleDto> getMyRoles(@RequestHeader("Authorization") String accessToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PatchMapping("/{user_id}/status")
    public ResponseEntity<Void> changeUserStatus(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable(name = "user_id") UUID userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
