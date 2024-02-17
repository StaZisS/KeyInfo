package org.example.key_info.core.accommodation;

import java.util.List;
import java.util.Optional;

public interface AccommodationRepository {
    void createAccommodation(AccommodationEntity entity);
    void deleteAccommodation(int buildId, int roomId);
    List<AccommodationEntity> getAllAccommodations();
    Optional<AccommodationEntity> getAccommodation(int buildId, int roomId);
    List<Integer> getBuildings();
    List<Integer> getRooms(int buildId);
}
