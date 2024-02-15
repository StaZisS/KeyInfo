package org.example.key_info.rest.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Для работы с пользователями")
public class RestUserController {

    @Operation(summary = "Получить всех пользователей")
    @GetMapping()
    public void getAllUsers(@RequestHeader("Authorization") String accessToken,
                            @RequestHeader(required = false) boolean name,
                            @RequestHeader(required = false) boolean email) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
