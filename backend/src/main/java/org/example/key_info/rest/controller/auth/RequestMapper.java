package org.example.key_info.rest.controller.auth;

import org.example.key_info.public_interface.client.ClientCreateDto;

public class RequestMapper {
    public static ClientCreateDto mapRequestToDto(RegisterDto dto) {
        return new ClientCreateDto(
                dto.name(),
                dto.email(),
                dto.password(),
                dto.gender()
        );
    }
}
