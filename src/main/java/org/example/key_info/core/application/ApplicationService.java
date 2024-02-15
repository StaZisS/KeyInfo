package org.example.key_info.core.application;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.repository.ClientRepository;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.public_interface.application.AcceptApplicationDto;
import org.example.key_info.public_interface.application.ApplicationDto;
import org.example.key_info.public_interface.application.CreateApplicationDto;
import org.example.key_info.public_interface.application.DeclineApplicationDto;
import org.example.key_info.public_interface.application.DeleteApplicationDto;
import org.example.key_info.public_interface.application.GetAllApplicationsDto;
import org.example.key_info.public_interface.application.GetMyApplicationDto;
import org.example.key_info.public_interface.application.UpdateApplicationDto;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private static final List<ClientRole> ROLES_CONTROL_APPLICATION = List.of(ClientRole.DEANERY, ClientRole.ADMIN);

    private final ApplicationRepository applicationRepository;
    private final ClientRepository clientRepository;

    public List<ApplicationDto> getMyApplications(GetMyApplicationDto dto) {
        var applications = applicationRepository.getMyApplication(dto.clientId(), dto.filter());

        return applications.parallelStream()
                .map(this::mapEntityToDto)
                .toList();
    }

    public List<ApplicationDto> getAllApplications(GetAllApplicationsDto dto) {
        checkClientRoles(dto.clientRoles());

        var applications = applicationRepository.getAllApplication(dto.filter());

        return applications.parallelStream()
                .map(this::mapEntityToDto)
                .toList();
    }

    //TODO: если есть бронь на время от преподавателя то заявку студента должна отклоняться
    public UUID createApplication(CreateApplicationDto dto) {
        var role = ClientRole.getClientRoleByName(dto.role());
        var isClientNotHaveThisRole = !dto.clientRoles().contains(role);
        if(isClientNotHaveThisRole) {
            throw new ExceptionInApplication("У вас нет такой роли", ExceptionType.INVALID);
        }

        if (role != ClientRole.TEACHER && dto.isDuplicate()) {
            throw new ExceptionInApplication("Только учитель может создавать дублирющиеся заявки", ExceptionType.INVALID);
        }

        if(dto.isDuplicate() && dto.endTimeToDuplicate() == null) {
            throw new ExceptionInApplication("Необходимо назначить дату док которой дублировать заявку", ExceptionType.INVALID);
        }

        var applicationEntity = new ApplicationEntity(
                null,
                dto.clientId(),
                dto.startTime(),
                dto.endTime(),
                ApplicationStatus.IN_PROCESS,
                OffsetDateTime.now(),
                dto.buildId(),
                dto.roomId(),
                dto.isDuplicate(),
                dto.endTimeToDuplicate()
        );

        return applicationRepository.createApplication(applicationEntity);
    }

    public void deleteApplication(DeleteApplicationDto dto) {
        var application = applicationRepository.getApplication(dto.applicationId())
                .orElseThrow(() -> new ExceptionInApplication("Заявка не найдена", ExceptionType.NOT_FOUND));

        if(dto.clientId() != application.applicationCreatorId()) {
            throw new ExceptionInApplication("Вы не можете удалить чужую заявку", ExceptionType.INVALID);
        }

        if(application.status() != ApplicationStatus.IN_PROCESS) {
            throw new ExceptionInApplication("Вы не можете удалить завершенную заявку", ExceptionType.INVALID);
        }

        applicationRepository.deleteApplication(application.applicationId());
    }

    public void updateApplication(UpdateApplicationDto dto) {
        var application = applicationRepository.getApplication(dto.applicationId())
                .orElseThrow(() -> new ExceptionInApplication("Заявка не найдена", ExceptionType.NOT_FOUND));

        if(dto.clientId() != application.applicationCreatorId()) {
            throw new ExceptionInApplication("Вы не можете изменить чужую заявку", ExceptionType.INVALID);
        }

        if(application.status() != ApplicationStatus.IN_PROCESS) {
            throw new ExceptionInApplication("Вы не можете изменить завершенную заявку", ExceptionType.INVALID);
        }

        var updatedApplication = new ApplicationEntity(
                dto.applicationId(),
                dto.clientId(),
                dto.startTime(),
                dto.endTime(),
                application.status(),
                application.createdTime(),
                dto.buildId(),
                dto.roomId(),
                dto.isDuplicate(),
                dto.endTimeToDuplicate()
        );
        applicationRepository.updateApplication(updatedApplication);
    }

    public void acceptApplication(AcceptApplicationDto dto) {
        checkClientRoles(dto.clientRoles());
        var application = applicationRepository.getApplication(dto.applicationId())
                .orElseThrow(() -> new ExceptionInApplication("Заявка не найдена", ExceptionType.NOT_FOUND));

        var updatedApplication = new ApplicationEntity(
                application.applicationId(),
                application.applicationCreatorId(),
                application.startTime(),
                application.endTime(),
                ApplicationStatus.ACCEPTED,
                application.createdTime(),
                application.buildId(),
                application.roomId(),
                application.isDuplicate(),
                application.endTimeToDuplicate()
        );
        applicationRepository.updateApplication(updatedApplication);

        if (application.isDuplicate())
            handleDuplicateApplication(application);
    }

    public void declineApplication(DeclineApplicationDto dto) {
        checkClientRoles(dto.clientRoles());
        var application = applicationRepository.getApplication(dto.applicationId())
                .orElseThrow(() -> new ExceptionInApplication("Заявка не найдена", ExceptionType.NOT_FOUND));

        var updatedApplication = new ApplicationEntity(
                application.applicationId(),
                application.applicationCreatorId(),
                application.startTime(),
                application.endTime(),
                ApplicationStatus.DECLINED,
                application.createdTime(),
                application.buildId(),
                application.roomId(),
                application.isDuplicate(),
                application.endTimeToDuplicate()
        );
        applicationRepository.updateApplication(updatedApplication);
    }

    private void handleDuplicateApplication(ApplicationEntity entity) {
        //TODO: Обработать повторяющиеся заявки
    }

    private ApplicationDto mapEntityToDto(ApplicationEntity entity) {
        var clientEntity = clientRepository.getClientByClientId(entity.applicationCreatorId())
                .orElseThrow(() -> new ExceptionInApplication("Пользователь не найден", ExceptionType.NOT_FOUND));

        return new ApplicationDto(
                entity.applicationId(),
                entity.applicationCreatorId(),
                entity.startTime(),
                entity.endTime(),
                entity.status(),
                entity.createdTime(),
                entity.buildId(),
                entity.roomId(),
                entity.isDuplicate(),
                entity.endTimeToDuplicate(),
                clientEntity.name(),
                clientEntity.email()
        );
    }

    private void checkClientRoles(Set<ClientRole> clientRoles) {
        if(!isClientHaveRoles(clientRoles)) {
            throw new ExceptionInApplication(String.format("Пользователь с правами %s не может выполнить это действие", clientRoles), ExceptionType.INVALID);
        }
    }

    private boolean isClientHaveRoles(Set<ClientRole> clientRoles) {
        return clientRoles
                .stream()
                .anyMatch(ROLES_CONTROL_APPLICATION::contains);
    }
}
