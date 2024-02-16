package org.example.key_info.core.schedule;

import lombok.AllArgsConstructor;
import org.example.key_info.core.application.ApplicationEntity;
import org.example.key_info.core.application.ApplicationFilterDto;
import org.example.key_info.core.application.ApplicationRepository;
import org.example.key_info.core.application.ApplicationStatus;
import org.example.key_info.core.client.repository.ClientEntity;
import org.example.key_info.core.client.repository.ClientRepository;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.rest.controller.schedule.DayTimeSlots;
import org.example.key_info.rest.controller.schedule.TimeSlot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ApplicationRepository applicationRepository;
    private final ScheduleRepository scheduleRepository;
    private final ClientRepository clientRepository;

    public List<DayTimeSlots> getFreeTimeSlots(GetFreeTimeSlotsDto getFreeTimeSlotsDto) {
        List<TimeSlotEntity> timeSlots = scheduleRepository.getFreeTimeSlots(getFreeTimeSlotsDto);

        // фулл заняты
        List<ApplicationEntity> busyApplicationsFirst = applicationRepository.getAllApplication(
                ApplicationFilterDto.builder()
                        .start(getFreeTimeSlotsDto.startTime())
                        .end(getFreeTimeSlotsDto.endTime())
                        .roomId(getFreeTimeSlotsDto.roomId())
                        .buildId(getFreeTimeSlotsDto.buildId())
                        .status(ApplicationStatus.ACCEPTED)
                        .build()
        );

        List<ApplicationEntity> waitingApplications = applicationRepository.getAllApplication(
                ApplicationFilterDto.builder()
                        .start(getFreeTimeSlotsDto.startTime())
                        .end(getFreeTimeSlotsDto.endTime())
                        .roomId(getFreeTimeSlotsDto.roomId())
                        .buildId(getFreeTimeSlotsDto.buildId())
                        .status(ApplicationStatus.IN_PROCESS)
                        .build()
        );

        List<ClientEntity> clients = clientRepository.getClientsByRole(ClientRole.TEACHER);

        List<ApplicationEntity> busyApplicationSecond =  waitingApplications.stream()
                .filter(applicationEntity -> clients.stream()
                        .anyMatch(clientEntity -> clientEntity.clientId().equals(applicationEntity.applicationCreatorId())))
                .toList();


        var result = timeSlots.stream()
                .filter(timeSlotEntity -> busyApplicationsFirst.stream()
                        .noneMatch(applicationEntity -> applicationEntity.startTime().isBefore(timeSlotEntity.endTime()) &&
                                applicationEntity.endTime().isAfter(timeSlotEntity.startTime())))
                .filter(timeSlotEntity -> busyApplicationSecond.stream()
                        .noneMatch(applicationEntity -> applicationEntity.startTime().isBefore(timeSlotEntity.endTime()) &&
                                applicationEntity.endTime().isAfter(timeSlotEntity.startTime())))
                .map(timeSlotEntity -> new TimeSlot(
                        timeSlotEntity.startTime(),
                        timeSlotEntity.endTime()
                ))
                .toList();

        throw new UnsupportedOperationException();

    }

    // //  Помимо этого, если студент попробует отправить заявку на время,
    //    //  уже забронированное преподавателем, заявка должна быть автоматически отклонена
}
