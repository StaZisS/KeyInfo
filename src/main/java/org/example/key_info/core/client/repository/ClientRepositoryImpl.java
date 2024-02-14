package org.example.key_info.core.client.repository;

import com.example.shop.public_.tables.records.ClientRecord;
import lombok.RequiredArgsConstructor;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.shop.public_.tables.Client.CLIENT;
import static com.example.shop.public_.tables.Role.ROLE;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {
    private final DSLContext create;

    @Override
    public Optional<UUID> createClient(ClientEntity entity) {
        var userIdRecord = create.insertInto(CLIENT)
                .set(CLIENT.NAME, entity.name())
                .set(CLIENT.EMAIL, entity.email())
                .set(CLIENT.PASSWORD, entity.password())
                .set(CLIENT.GENDER, entity.gender())
                .set(CLIENT.CREATED_DATE, entity.createdDate())
                .returning(CLIENT.CLIENT_ID)
                .fetchOne();

        var userId = userIdRecord.getClientId();

        entity.role().forEach(r -> insertUserRole(r, userId, create));

        return Optional.of(userId);
    }

    @Override
    public Optional<ClientEntity> getClientByEmail(String email) {
        var clientEntityRecordOptional = create.selectFrom(CLIENT)
                .where(CLIENT.EMAIL.eq(email))
                .fetchOptional();
        if(clientEntityRecordOptional.isEmpty()) {
            return Optional.empty();
        }
        var clientEntityRecord = clientEntityRecordOptional.get();
        var clientRoles = getClientRoles(clientEntityRecord.getClientId(), create);

        return Optional.of(mapClientEntity(clientEntityRecord, clientRoles));
    }

    @Override
    public Optional<ClientEntity> getClientByClientId(UUID clientId) {
        var clientEntityRecordOptional = create.selectFrom(CLIENT)
                .where(CLIENT.CLIENT_ID.eq(clientId))
                .fetchOptional();
        if(clientEntityRecordOptional.isEmpty()) {
            return Optional.empty();
        }
        var clientEntityRecord = clientEntityRecordOptional.get();
        var clientRoles = getClientRoles(clientEntityRecord.getClientId(), create);

        return Optional.of(mapClientEntity(clientEntityRecord, clientRoles));
    }

    private void insertUserRole(ClientRole role, UUID clientId, DSLContext localCtx) {
        localCtx.insertInto(ROLE)
                .set(ROLE.CLIENT_ID, clientId)
                .set(ROLE.ROLE_, role.name())
                .execute();
    }

    private Set<ClientRole> getClientRoles(UUID clientId, DSLContext localCtx) {
        return localCtx.selectFrom(ROLE)
                .where(ROLE.CLIENT_ID.eq(clientId))
                .fetchStream()
                .map(row -> ClientRole.getClientRoleByName(row.getRole()))
                .collect(Collectors.toSet());
    }

    private ClientEntity mapClientEntity(ClientRecord clientRecord, Set<ClientRole> roles) {
        return  new ClientEntity(
                clientRecord.getClientId(),
                clientRecord.getName(),
                clientRecord.getEmail(),
                clientRecord.getPassword(),
                clientRecord.getGender(),
                clientRecord.getCreatedDate(),
                roles
        );
    }
}
