package org.example.key_info.core.role;

import org.example.key_info.core.client.repository.ClientRole;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository {
    void assignRoleToUser(UUID userId, ClientRole role);
    void removeRoleFromUser(UUID userId, ClientRole role);
    List<ClientRole> getUserRoles(UUID userId);
}
