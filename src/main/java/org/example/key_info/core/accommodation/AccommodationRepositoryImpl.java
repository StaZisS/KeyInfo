package org.example.key_info.core.accommodation;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.shop.public_.tables.Studyroom.STUDYROOM;

@Repository
@RequiredArgsConstructor
public class AccommodationRepositoryImpl implements AccommodationRepository {
    private final DSLContext create;

    @Override
    public void createAccommodation(AccommodationEntity entity) {
        create.insertInto(STUDYROOM)
                .set(STUDYROOM.BUILD, entity.buildId())
                .set(STUDYROOM.ROOM, entity.roomId())
                .execute();
    }

    @Override
    public void deleteAccommodation(int buildId, int roomId) {
        create.deleteFrom(STUDYROOM)
                .where(STUDYROOM.BUILD.eq(buildId))
                .and(STUDYROOM.ROOM.eq(roomId))
                .execute();
    }

    @Override
    public List<AccommodationEntity> getAllAccommodations() {
        return create.selectFrom(STUDYROOM)
                .fetch()
                .map(record -> new AccommodationEntity(record.get(STUDYROOM.BUILD), record.get(STUDYROOM.ROOM)));
    }

    @Override
    public Optional<AccommodationEntity> getAccommodation(int buildId, int roomId) {
        var record = create.selectFrom(STUDYROOM)
                .where(STUDYROOM.BUILD.eq(buildId))
                .and(STUDYROOM.ROOM.eq(roomId))
                .fetchOptional();
        return record.map(r -> new AccommodationEntity(r.get(STUDYROOM.BUILD), r.get(STUDYROOM.ROOM)));
    }

    @Override
    public List<Integer> getBuildings() {
        return create.selectDistinct(STUDYROOM.BUILD)
                .fetch(Record1::value1);
    }

    @Override
    public List<Integer> getRooms() {
        return create.selectDistinct(STUDYROOM.ROOM)
                .fetch(Record1::value1);
    }
}
