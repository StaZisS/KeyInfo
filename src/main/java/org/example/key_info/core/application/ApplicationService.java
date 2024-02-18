package org.example.key_info.core.application;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.repository.ClientRepository;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.core.schedule.TimeSlotEntity;
import org.example.key_info.core.timeslot.TimeSlotService;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private static final List<ClientRole> ROLES_CONTROL_APPLICATION = List.of(ClientRole.DEANERY, ClientRole.ADMIN);

    private final TimeSlotService timeSlotService;
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

    @Transactional
    public UUID createApplication(CreateApplicationDto dto) {
        var role = ClientRole.getClientRoleByName(dto.role());
        var isClientNotHaveThisRole = !dto.clientRoles().contains(role);
        if(isClientNotHaveThisRole) {
            throw new ExceptionInApplication("У вас нет такой роли", ExceptionType.INVALID);
        }

        if (role != ClientRole.TEACHER && dto.isDuplicate()) {
            throw new ExceptionInApplication("Только учитель может создавать дублирющиеся заявки", ExceptionType.INVALID);
        }

        if (dto.isDuplicate() && dto.endTimeToDuplicate() == null) {
            throw new ExceptionInApplication("Необходимо назначить дату до которой дублировать заявку", ExceptionType.INVALID);
        }

        if (dto.startTime().isBefore(OffsetDateTime.now())) {
            throw new ExceptionInApplication("Заявка не может быть назначена на уже начавшуюся пару", ExceptionType.INVALID);
        }

        var teacherApplications = applicationRepository.getAcceptedTeacherApplications(dto.buildId(), dto.roomId(), dto.startTime(), dto.endTime());
        if (!teacherApplications.isEmpty() && role == ClientRole.STUDENT) {
            throw new ExceptionInApplication("В это время уже занято учителем", ExceptionType.INVALID);
        }

        checkDuplicateApplication(dto);

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

        var timeslot = new TimeSlotEntity(dto.startTime(), dto.endTime());
        timeSlotService.createTimeslot(timeslot);

        return applicationRepository.createApplication(applicationEntity);
    }

    public void deleteApplication(DeleteApplicationDto dto) {
        var application = applicationRepository.getApplication(dto.applicationId())
                .orElseThrow(() -> new ExceptionInApplication("Заявка не найдена", ExceptionType.NOT_FOUND));

        if(!dto.clientId().equals(application.applicationCreatorId())) {
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

        if(!dto.clientId().equals(application.applicationCreatorId())) {
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

        var timeslot = new TimeSlotEntity(dto.startTime(), dto.endTime());
        timeSlotService.createTimeslot(timeslot);

        applicationRepository.updateApplication(updatedApplication);
    }

    public void acceptApplication(AcceptApplicationDto dto) {
        checkClientRoles(dto.clientRoles());
        var application = applicationRepository.getApplication(dto.applicationId())
                .orElseThrow(() -> new ExceptionInApplication("Заявка не найдена", ExceptionType.NOT_FOUND));

        if(application.status() != ApplicationStatus.IN_PROCESS) {
            throw new ExceptionInApplication("Вы не можете изменить завершенную заявку", ExceptionType.INVALID);
        }

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

        if (updatedApplication.isDuplicate()) {
            OffsetDateTime startTime = updatedApplication.startTime().plusDays(7);
            OffsetDateTime endTime = updatedApplication.endTime().plusDays(7);

            while (endTime.isBefore(updatedApplication.endTimeToDuplicate())) {
                var tempApplication = new ApplicationEntity(
                        null,
                        updatedApplication.applicationCreatorId(),
                        startTime,
                        endTime,
                        ApplicationStatus.ACCEPTED,
                        OffsetDateTime.now(),
                        updatedApplication.buildId(),
                        updatedApplication.roomId(),
                        true,
                        updatedApplication.endTimeToDuplicate()
                );
                var timeslot = new TimeSlotEntity(startTime, endTime);
                timeSlotService.createTimeslot(timeslot);
                applicationRepository.createApplication(tempApplication);

                startTime = startTime.plusDays(7);
                endTime = endTime.plusDays(7);
            }
        }

        var filter = ApplicationFilterDto.builder()
                .start(application.startTime())
                .end(application.endTime())
                .buildId(application.buildId())
                .roomId(application.roomId())
                .status(ApplicationStatus.IN_PROCESS)
                .build();

        var applications = applicationRepository.getAllApplication(filter);

        for (ApplicationEntity app : applications) {
            var updatedApp = new ApplicationEntity(
                    app.applicationId(),
                    app.applicationCreatorId(),
                    app.startTime(),
                    app.endTime(),
                    ApplicationStatus.DECLINED,
                    app.createdTime(),
                    app.buildId(),
                    app.roomId(),
                    app.isDuplicate(),
                    app.endTimeToDuplicate()
            );
            applicationRepository.updateApplication(updatedApp);
        }

        if (application.isDuplicate()) {
            handleDuplicateApplication(application);
        }
    }

    public void declineApplication(DeclineApplicationDto dto) {
        checkClientRoles(dto.clientRoles());
        var application = applicationRepository.getApplication(dto.applicationId())
                .orElseThrow(() -> new ExceptionInApplication("Заявка не найдена", ExceptionType.NOT_FOUND));

        if(application.status() != ApplicationStatus.IN_PROCESS) {
            throw new ExceptionInApplication("Вы не можете изменить завершенную заявку", ExceptionType.INVALID);
        }

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

    private void checkDuplicateApplication(CreateApplicationDto dto) {
        var filter = new ApplicationFilterDto(ApplicationStatus.IN_PROCESS, dto.startTime(), dto.endTime(), dto.buildId(), dto.roomId());
        var applications = applicationRepository.getMyApplication(dto.clientId(), filter);
        if (!applications.isEmpty()) {
            throw new ExceptionInApplication("Нельзя создавать дублирующиеся заявки", ExceptionType.INVALID);
        }
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
