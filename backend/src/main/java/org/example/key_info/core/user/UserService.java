package org.example.key_info.core.user;

import lombok.AllArgsConstructor;
import org.example.key_info.core.client.repository.ClientEntity;
import org.example.key_info.core.client.repository.ClientInfo;
import org.example.key_info.core.client.repository.ClientRepository;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.public_interface.client.ClientProfileDto;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final ClientRepository clientRepository;

    public List<ClientProfileDto> getAllUsers(GetUsersDto getUsersDto) {
        if (getUsersDto.clientRoles().stream()
                .anyMatch(role -> role.equals(ClientRole.ADMIN) || role.equals(ClientRole.DEANERY)
                        || role.equals(ClientRole.TEACHER))) {
            return getAll(getUsersDto);
        } else if (getUsersDto.clientRoles().stream()
                .anyMatch(role -> role.equals(ClientRole.STUDENT))) {
            return getStudents(getUsersDto);
        } else {
            throw new ExceptionInApplication("У вас нет прав для просмотра пользователей", ExceptionType.FORBIDDEN);
        }
    }

    private List<ClientProfileDto> getAll(GetUsersDto getUsersDto) {
        var result = getClientEntities(getUsersDto);

        return result.stream()
                .map(client -> new ClientProfileDto(client.clientId(), client.name(), client.email(),
                        client.gender(), client.createdDate(), client.role()))
                .toList();
    }

    private List<ClientProfileDto> getStudents(GetUsersDto getUsersDto) {
        var result = getClientEntities(getUsersDto);

        return result.stream()
                .filter(client -> client.role().stream().allMatch(role -> role.equals(ClientRole.STUDENT) ||
                        role.equals(ClientRole.UNSPECIFIED)))
                .map(client -> new ClientProfileDto(null, client.name(), null,
                        client.gender(), client.createdDate(), client.role()))
                .toList();
    }

    private List<ClientEntity> getClientEntities(GetUsersDto getUsersDto) {
        var clientInfo = new ClientInfo(getUsersDto.name(), getUsersDto.email());
        return clientRepository.getAllClients(clientInfo);
    }
}
