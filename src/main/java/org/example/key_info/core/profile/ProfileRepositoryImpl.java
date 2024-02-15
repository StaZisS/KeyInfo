package org.example.key_info.core.profile;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.example.shop.public_.tables.Client.CLIENT;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {
    private final DSLContext create;

    @Override
    public void updateProfile(UpdateProfileDto updateProfileDto) {
        create.update(CLIENT)
                .set(CLIENT.NAME, updateProfileDto.name())
                .set(CLIENT.EMAIL, updateProfileDto.email())
                .set(CLIENT.PASSWORD, updateProfileDto.password())
                .where(CLIENT.CLIENT_ID.eq(updateProfileDto.userId()))
                .execute();
    }
}
