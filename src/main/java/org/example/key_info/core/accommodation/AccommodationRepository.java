package org.example.key_info.core.accommodation;

import java.util.List;

public interface AccommodationRepository {
    void createAccommodation(AccommodationEntity entity);
    void deleteAccommodation(int buildId, int roomId);
    List<AccommodationEntity> getAllAccommodations();
}
