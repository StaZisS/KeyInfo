package org.example.key_info.core.accommodation;

public interface AccommodationRepository {
    void createAccommodation(AccommodationEntity entity);
    void deleteAccommodation(int buildId, int roomId);
}
