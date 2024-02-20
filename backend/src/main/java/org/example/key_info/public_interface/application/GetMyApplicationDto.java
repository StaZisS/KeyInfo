package org.example.key_info.public_interface.application;

import org.example.key_info.core.application.ApplicationFilterDto;

import java.util.UUID;

public record GetMyApplicationDto(
        ApplicationFilterDto filter,
        UUID clientId
) {
}
