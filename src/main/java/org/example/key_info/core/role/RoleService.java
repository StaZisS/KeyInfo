package org.example.key_info.core.role;

import lombok.AllArgsConstructor;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class RoleService {
    private static final Set<ClientRole> ASSIGN_DEANERIES_ROLES = Set.of(ClientRole.STUDENT, ClientRole.TEACHER);
    private final RoleRepository roleRepository;

    public void assignRoleToUser(UserStatusUpdateDto userStatusUpdateDto) {
        Set<ClientRole> appointUserRoles = userStatusUpdateDto.appointUserRoles();

        validateClientRoles(userStatusUpdateDto.clientRoles(), appointUserRoles);

        appointUserRoles.forEach(role -> roleRepository.assignRoleToUser(userStatusUpdateDto.appointUserId(), role));
        roleRepository.removeRoleFromUser(userStatusUpdateDto.appointUserId(), ClientRole.UNSPECIFIED);
    }

    private void validateClientRoles(Set<ClientRole> roles, Set<ClientRole> appointUserRoles) {
        if (isClientHaveAdminRole(roles)) {
            return;
        }

        if (isClientHaveDeaneryRole(roles)) {
            appointUserRoles.forEach(role -> {
                if (!ASSIGN_DEANERIES_ROLES.contains(role)) {
                    throw new ExceptionInApplication(String.format("Пользователь с ролью %s не может назначать роль %s",
                            roles, role), ExceptionType.INVALID);
                }
            });

            return;
        }

        throw new ExceptionInApplication(String.format("Пользователь с ролью %s не может назначать роли %s",
                roles, appointUserRoles), ExceptionType.INVALID);
    }


    private boolean isClientHaveAdminRole(Set<ClientRole> clientRoles) {
        return clientRoles.contains(ClientRole.ADMIN);
    }

    private boolean isClientHaveDeaneryRole(Set<ClientRole> clientRoles) {
        return clientRoles.contains(ClientRole.DEANERY);
    }
}
