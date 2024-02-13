package org.example.key_info.core.client.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.example.shop.public_.tables.Client.CLIENT;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {
    private final DSLContext create;
    private final ClientEntityMapper clientEntityMapper = new ClientEntityMapper();

    @Override
    public Optional<UUID> createClient(ClientEntity entity) {
        return create.insertInto(CLIENT)
                .set(CLIENT.NAME, entity.name())
                .set(CLIENT.EMAIL, entity.email())
                .set(CLIENT.PASSWORD, entity.password())
                .set(CLIENT.GENDER, entity.gender())
                .set(CLIENT.CREATED_DATE, entity.createdDate())
                .set(CLIENT.ROLE, entity.role().name())
                .returning(CLIENT.CLIENT_ID)
                .fetchOptional(r -> r.get(CLIENT.CLIENT_ID));
    }

    @Override
    public Optional<ClientEntity> getClientByEmail(String email) {
        return create.selectFrom(CLIENT)
                .where(CLIENT.EMAIL.eq(email))
                .fetchOptional()
                .map(clientEntityMapper);
    }

    @Override
    public Optional<ClientEntity> getClientByClientId(UUID clientId) {
        return create.selectFrom(CLIENT)
                .where(CLIENT.CLIENT_ID.eq(clientId))
                .fetchOptional()
                .map(clientEntityMapper);
    }
}
