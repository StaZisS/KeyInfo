package org.example.key_info.core.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository {
    List<ApplicationEntity> getMyApplication(UUID clientId, ApplicationFilterDto filter);
    List<ApplicationEntity> getAllApplication(ApplicationFilterDto filter);
    UUID createApplication(ApplicationEntity entity);
    void deleteApplication(UUID applicationId);
    void updateApplication(ApplicationEntity entity);
    Optional<ApplicationEntity> getApplication(UUID applicationId);
}
