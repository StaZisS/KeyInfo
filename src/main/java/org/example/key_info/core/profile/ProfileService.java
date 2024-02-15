package org.example.key_info.core.profile;

import lombok.AllArgsConstructor;
import org.example.key_info.core.client.repository.ClientEntity;
import org.example.key_info.core.client.repository.ClientRepository;
import org.example.key_info.core.util.PasswordTool;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.example.key_info.public_interface.profile.ProfileDto;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ClientRepository clientRepository;

    public ProfileDto updateProfile(UpdateProfileDto updateProfileDto) {
        var formattedDto = formatUpdateProfileDto(updateProfileDto);

        updateUserProfile(formattedDto);

        ClientEntity clientEntity = clientRepository.getClientByClientId(updateProfileDto.userId())
                .orElseThrow(() -> new ExceptionInApplication("Клиент не найден", ExceptionType.NOT_FOUND));

        return new ProfileDto(
                clientEntity.name(),
                clientEntity.email(),
                clientEntity.password()
        );
    }

    private void updateUserProfile(UpdateProfileDto formattedDto) {
        try {
            profileRepository.updateProfile(formattedDto);
        } catch (Exception e) {
            throw new ExceptionInApplication("Используйте другую почту", ExceptionType.FATAL);
        }
    }

    private UpdateProfileDto formatUpdateProfileDto(UpdateProfileDto profileDto) {
        return new UpdateProfileDto(
                profileDto.userId(),
                profileDto.name(),
                profileDto.email(),
                PasswordTool.getHashPassword(profileDto.password())
        );
    }

}
