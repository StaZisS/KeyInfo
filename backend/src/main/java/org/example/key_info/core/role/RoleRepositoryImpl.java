package org.example.key_info.core.role;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.repository.ClientRole;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.example.shop.public_.Tables.ROLE;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    private final RoleEntityMapper roleEntityMapper = new RoleEntityMapper();
    private final DSLContext create;

    @Override
    public void assignRoleToUser(UUID userId, ClientRole role) {
        create.insertInto(ROLE)
                .set(ROLE.CLIENT_ID, userId)
                .set(ROLE.ROLE_, role.name())
                .execute();
    }

    @Override
    public void removeRoleFromUser(UUID userId, ClientRole role) {
        create.deleteFrom(ROLE)
                .where(ROLE.CLIENT_ID.eq(userId))
                .and(ROLE.ROLE_.eq(role.name()))
                .execute();
    }

    @Override
    public List<ClientRole> getUserRoles(UUID userId) {
        return create.selectFrom(ROLE)
                .where(ROLE.CLIENT_ID.eq(userId))
                .fetch(roleEntityMapper);

    }
}
