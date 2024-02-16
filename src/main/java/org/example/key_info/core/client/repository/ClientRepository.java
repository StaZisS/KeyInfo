package org.example.key_info.core.client.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    Optional<UUID> createClient(ClientEntity entity);
    Optional<ClientEntity> getClientByEmail(String email);
    Optional<ClientEntity> getClientByClientId(UUID clientId);
    List<ClientEntity> getClientsByRole(ClientRole role);
    List<ClientEntity> getAllClients(ClientInfo info);
}
