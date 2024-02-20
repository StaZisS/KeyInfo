package org.example.key_info.rest.controller.deanery;

import java.util.UUID;

public record ChangePrivateKeyDto(
        UUID keyId,
        boolean isPrivate
) {
}
