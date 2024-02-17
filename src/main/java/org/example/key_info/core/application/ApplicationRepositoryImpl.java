package org.example.key_info.core.application;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.repository.ClientRole;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.shop.public_.tables.Request.REQUEST;
import static com.example.shop.public_.tables.Role.ROLE;

@Repository
@RequiredArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepository {
    private final DSLContext create;

    private final ApplicationEntityMapper applicationEntityMapper = new ApplicationEntityMapper();

    @Override
    public List<ApplicationEntity> getMyApplication(UUID clientId, ApplicationFilterDto filter) {
        var filterCondition = getFilterCondition(filter);
        return create.selectFrom(REQUEST)
                .where(REQUEST.REQUEST_CREATOR.eq(clientId))
                .and(filterCondition)
                .fetch(applicationEntityMapper);
    }

    @Override
    public List<ApplicationEntity> getAllApplication(ApplicationFilterDto filter) {
        var filterCondition = getFilterCondition(filter);
        return create.selectFrom(REQUEST)
                .where(filterCondition)
                .fetch(applicationEntityMapper);
    }

    @Override
    public UUID createApplication(ApplicationEntity entity) {
        return create.insertInto(REQUEST)
                .set(REQUEST.REQUEST_CREATOR, entity.applicationCreatorId())
                .set(REQUEST.START_TIME, entity.startTime())
                .set(REQUEST.END_TIME, entity.endTime())
                .set(REQUEST.STATUS, entity.status().name())
                .set(REQUEST.CREATED_TIME, entity.createdTime())
                .set(REQUEST.ROOM, entity.roomId())
                .set(REQUEST.BUILD, entity.buildId())
                .set(REQUEST.DUPLICATE, entity.isDuplicate())
                .set(REQUEST.END_TIME_DUPLICATE, entity.endTimeToDuplicate())
                .returning(REQUEST.REQUEST_ID)
                .fetchOne(REQUEST.REQUEST_ID);
    }

    @Override
    public void deleteApplication(UUID applicationId) {
        create.deleteFrom(REQUEST)
                .where(REQUEST.REQUEST_ID.eq(applicationId))
                .execute();
    }

    @Override
    public void updateApplication(ApplicationEntity entity) {
        create.update(REQUEST)
                .set(REQUEST.REQUEST_CREATOR, entity.applicationCreatorId())
                .set(REQUEST.START_TIME, entity.startTime())
                .set(REQUEST.END_TIME, entity.endTime())
                .set(REQUEST.STATUS, entity.status().name())
                .set(REQUEST.CREATED_TIME, entity.createdTime())
                .set(REQUEST.ROOM, entity.roomId())
                .set(REQUEST.BUILD, entity.buildId())
                .set(REQUEST.DUPLICATE, entity.isDuplicate())
                .set(REQUEST.END_TIME_DUPLICATE, entity.endTimeToDuplicate())
                .where(REQUEST.REQUEST_ID.eq(entity.applicationId()))
                .execute();
    }

    @Override
    public Optional<ApplicationEntity> getApplication(UUID applicationId) {
        return create.selectFrom(REQUEST)
                .where(REQUEST.REQUEST_ID.eq(applicationId))
                .fetchOptional(applicationEntityMapper);
    }

    @Override
    public List<ApplicationEntity> getAcceptedTeacherApplications(int buildId, int roomId, OffsetDateTime startTime, OffsetDateTime endTime) {
        return create.selectFrom(REQUEST)
                .where(REQUEST.BUILD.eq(buildId))
                .and(REQUEST.ROOM.eq(roomId))
                .and(REQUEST.START_TIME.eq(startTime))
                .and(REQUEST.END_TIME.eq(endTime))
                .and(REQUEST.STATUS.eq(ApplicationStatus.ACCEPTED.name()))
                .fetchStream()
                .map(applicationEntityMapper)
                .filter(a ->
                        create.selectFrom(ROLE)
                        .where(ROLE.CLIENT_ID.eq(a.applicationCreatorId()))
                        .and(ROLE.ROLE_.eq(ClientRole.TEACHER.name()))
                        .fetch()
                        .isNotEmpty()
                )
                .toList();
    }

    private Condition getFilterCondition(ApplicationFilterDto filter) {
        Condition condition = DSL.trueCondition();

        if(filter.status() != null) {
            condition = condition.and(REQUEST.STATUS.eq(filter.status().name()));
        }
        if(filter.buildId() != null) {
            condition = condition.and(REQUEST.BUILD.eq(filter.buildId()));
        }
        if(filter.roomId() != null) {
            condition = condition.and(REQUEST.ROOM.eq(filter.roomId()));
        }
        if(filter.start() != null) {
            condition = condition.and(REQUEST.START_TIME.greaterOrEqual(filter.start()));
        }
        if(filter.end() != null) {
            condition = condition.and(REQUEST.END_TIME.lessOrEqual(filter.end()));
        }

        return condition;
    }
}
