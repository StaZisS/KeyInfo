package org.example.key_info.rest.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class RestUserController {

    @GetMapping()
    public void getAllUsers(@RequestHeader("Authorization") String accessToken,
                            @RequestHeader(required = false) boolean name,
                            @RequestHeader(required = false) boolean email) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
