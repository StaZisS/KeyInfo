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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ApplicationRepository applicationRepository;
    private final ScheduleRepository scheduleRepository;
    private final ClientRepository clientRepository;

    public List<DayTimeSlots> getFreeTimeSlots(GetFreeTimeSlotsDto getFreeTimeSlotsDto) {
        List<TimeSlotEntity> timeSlots = scheduleRepository.getFreeTimeSlots(getFreeTimeSlotsDto);

        List<ApplicationEntity> busyAcceptedApplications = getAcceptedApplication(getFreeTimeSlotsDto);
        List<ApplicationEntity> waitingApplications = getWaitingApplication(getFreeTimeSlotsDto);
        List<ClientEntity> clients = clientRepository.getClientsByRole(ClientRole.TEACHER);
        List<ApplicationEntity> busyWaitingApplicationByTeacher = getWaitingApplicationByTeacher(waitingApplications, clients);

        var freeTimeSlots = getListFreeTimeSlots(timeSlots, busyAcceptedApplications, busyWaitingApplicationByTeacher);

        var dayTimeSlots = freeTimeSlots.stream()
                .collect(Collectors.groupingBy(timeSlot -> timeSlot.startTime().toLocalDate()));

        return mapIntoDateTimeSlots(dayTimeSlots);
    }

    private List<ApplicationEntity> getAcceptedApplication(GetFreeTimeSlotsDto getFreeTimeSlotsDto) {
        return applicationRepository.getAllApplication(
                ApplicationFilterDto.builder()
                        .start(getFreeTimeSlotsDto.startTime())
                        .end(getFreeTimeSlotsDto.endTime())
                        .roomId(getFreeTimeSlotsDto.roomId())
                        .buildId(getFreeTimeSlotsDto.buildId())
                        .status(ApplicationStatus.ACCEPTED)
                        .build()
        );
    }

    private List<ApplicationEntity> getWaitingApplication(GetFreeTimeSlotsDto getFreeTimeSlotsDto) {
        return applicationRepository.getAllApplication(
                ApplicationFilterDto.builder()
                        .start(getFreeTimeSlotsDto.startTime())
                        .end(getFreeTimeSlotsDto.endTime())
                        .roomId(getFreeTimeSlotsDto.roomId())
                        .buildId(getFreeTimeSlotsDto.buildId())
                        .status(ApplicationStatus.IN_PROCESS)
                        .build()
        );
    }

    private static List<ApplicationEntity> getWaitingApplicationByTeacher(List<ApplicationEntity> waitingApplications,
                                                                          List<ClientEntity> clients) {
        return waitingApplications.stream()
                .filter(applicationEntity -> clients.stream()
                        .anyMatch(clientEntity -> clientEntity.clientId().equals(applicationEntity.applicationCreatorId())))
                .toList();
    }

    private static List<TimeSlot> getListFreeTimeSlots(List<TimeSlotEntity> timeSlots, List<ApplicationEntity> busyAcceptedApplications,
                                                       List<ApplicationEntity> busyWaitingApplicationByTeacher) {
        return timeSlots.stream()
                .filter(timeSlotEntity -> busyAcceptedApplications.stream()
                        .noneMatch(applicationEntity -> applicationEntity.startTime().isBefore(timeSlotEntity.endTime()) &&
                                applicationEntity.endTime().isAfter(timeSlotEntity.startTime())))
                .filter(timeSlotEntity -> busyWaitingApplicationByTeacher.stream()
                        .noneMatch(applicationEntity -> applicationEntity.startTime().isBefore(timeSlotEntity.endTime()) &&
                                applicationEntity.endTime().isAfter(timeSlotEntity.startTime())))
                .map(timeSlotEntity -> new TimeSlot(
                        timeSlotEntity.startTime(),
                        timeSlotEntity.endTime()
                ))
                .toList();
    }

    private static List<DayTimeSlots> mapIntoDateTimeSlots(Map<LocalDate, List<TimeSlot>> dayTimeSlots) {
        List<DayTimeSlots> result = new ArrayList<>();

        for (var entry : dayTimeSlots.entrySet()) {
            DayTimeSlots dayTimeSlot = DayTimeSlots.builder()
                    .time(OffsetDateTime.of(entry.getKey(), LocalTime.MIDNIGHT, OffsetDateTime.now().getOffset()))
                    .timeSlots(entry.getValue())
                    .build();
            result.add(dayTimeSlot);
        }

        return result;
    }
}
