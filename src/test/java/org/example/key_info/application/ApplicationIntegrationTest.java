package org.example.key_info.application;

import com.github.dockerjava.api.exception.NotFoundException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.key_info.core.application.ApplicationFilterDto;
import org.example.key_info.core.application.ApplicationService;
import org.example.key_info.core.application.ApplicationStatus;
import org.example.key_info.core.client.repository.ClientEntity;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.core.client.service.ClientService;
import org.example.key_info.public_interface.application.AcceptApplicationDto;
import org.example.key_info.public_interface.application.CreateApplicationDto;
import org.example.key_info.public_interface.application.DeclineApplicationDto;
import org.example.key_info.public_interface.application.DeleteApplicationDto;
import org.example.key_info.public_interface.application.GetAllApplicationsDto;
import org.example.key_info.public_interface.application.GetMyApplicationDto;
import org.example.key_info.public_interface.application.UpdateApplicationDto;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;

import static com.example.shop.public_.tables.Client.CLIENT;
import static com.example.shop.public_.tables.Role.ROLE;
import static com.example.shop.public_.tables.Studyroom.STUDYROOM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@SpringJUnitConfig(classes = {ApplicationIntegrationTestConfiguration.class})
@ActiveProfiles("test")
public class ApplicationIntegrationTest {
    private static final String CHANGELOG_FILE_PATH = "db/changelog/db.changelog-master.yaml";

    private static final ClientEntity STUDENT_ENTITY = formatClient(UUID.randomUUID(), Set.of(ClientRole.STUDENT));
    private static final ClientEntity TEACHER_ENTITY = formatClient(UUID.randomUUID(), Set.of(ClientRole.TEACHER));
    private static final ClientEntity DEANERY_ENTITY = formatClient(UUID.randomUUID(), Set.of(ClientRole.DEANERY));

    private static final int BUILD_ID = 1;
    private static final int ROOM_ID = 2;

    @Container
    private static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("KeyInfo")
            .withUsername("postgres")
            .withPassword("veryStrongPassword");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.password", database::getPassword);
        registry.add("spring.datasource.username", database::getUsername);
    }

    private static DSLContext dslContext;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ClientService clientService;

    @BeforeAll
    public static void setup() throws SQLException, LiquibaseException {
        migrate();

        dslContext = DSL.using(
                database.getJdbcUrl(),
                database.getUsername(),
                database.getPassword()
        );

        databasePreparation();
    }

    @Test
    public void createApplicationByStudent() {
        var timeSlot = TimeSlotEnum.FIRST;
        var year = 2000;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );

        applicationService.createApplication(createApplicationDto);
    }

    @Test
    public void createApplicationByTeacher() {
        var timeSlot = TimeSlotEnum.FIRST;
        var year = 2001;
        var createApplicationDto = new CreateApplicationDto(
                TEACHER_ENTITY.clientId(),
                TEACHER_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.TEACHER.name(),
                false,
                null
        );

        applicationService.createApplication(createApplicationDto);
    }

    @Test
    public void createDuplicateApplicationByStudent() {
        var timeSlot = TimeSlotEnum.FIRST;
        var year = 2002;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                true,
                OffsetDateTime.now()
        );

        assertThrows(ExceptionInApplication.class, () ->
                applicationService.createApplication(createApplicationDto)
        );
    }

    @Test
    public void createDuplicateApplicationByTeacher() {
        var timeSlot = TimeSlotEnum.FIRST;
        var year = 2003;
        var createApplicationDto = new CreateApplicationDto(
                TEACHER_ENTITY.clientId(),
                TEACHER_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.TEACHER.name(),
                true,
                OffsetDateTime.now()
        );

        applicationService.createApplication(createApplicationDto);
    }

    @Test
    public void getStudentApplications() {
        var timeSlot = TimeSlotEnum.SECOND;
        var year = 2006;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var applicationId = applicationService.createApplication(createApplicationDto);

        var getMyApplicationsDto = new GetMyApplicationDto(
                getDefaultFilter(),
                STUDENT_ENTITY.clientId()
        );

        var applications = applicationService.getMyApplications(getMyApplicationsDto);

        assertTrue(applications.stream().anyMatch(a -> a.applicationId().equals(applicationId)));
    }

    @Test
    public void tryGetAllApplicationByStudent() {
        var getAllApplication = new GetAllApplicationsDto(
                getDefaultFilter(),
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role()
        );

        assertThrows(ExceptionInApplication.class, () ->
                applicationService.getAllApplications(getAllApplication)
        );
    }

    @Test
    public void tryGetAllApplicationByTeacher() {
        var getAllApplication = new GetAllApplicationsDto(
                getDefaultFilter(),
                TEACHER_ENTITY.clientId(),
                TEACHER_ENTITY.role()
        );

        assertThrows(ExceptionInApplication.class, () ->
                applicationService.getAllApplications(getAllApplication)
        );
    }

    @Test
    public void getAllApplication() {
        var timeSlot = TimeSlotEnum.THIRD;
        var year = 2000;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );

        var applicationId = applicationService.createApplication(createApplicationDto);

        var getAllApplication = new GetAllApplicationsDto(
                getDefaultFilter(),
                DEANERY_ENTITY.clientId(),
                DEANERY_ENTITY.role()
        );

        var applications = applicationService.getAllApplications(getAllApplication);

        assertTrue(applications.stream().anyMatch(a -> a.applicationId().equals(applicationId)));
    }

    @Test
    public void deleteApplication() {
        var timeSlot = TimeSlotEnum.FIFTH;
        var year = 2000;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );


        var applicationId = applicationService.createApplication(createApplicationDto);

        var deleteApplicationDto = new DeleteApplicationDto(STUDENT_ENTITY.clientId(), applicationId);
        applicationService.deleteApplication(deleteApplicationDto);

        var getMyApplicationsDto = new GetMyApplicationDto(
                getDefaultFilter(),
                STUDENT_ENTITY.clientId()
        );
        var applications = applicationService.getMyApplications(getMyApplicationsDto);

        assertFalse(applications.stream().anyMatch(a -> a.applicationId().equals(applicationId)));
    }

    @Test
    public void updateApplication() {
        var timeSlot = TimeSlotEnum.SECOND;
        var year = 2008;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var applicationId = applicationService.createApplication(createApplicationDto);

        var updatedTimeSlot = TimeSlotEnum.THIRD;
        var updateApplicationDto = new UpdateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                applicationId,
                getStartTime(updatedTimeSlot, year),
                getEndTime(updatedTimeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        applicationService.updateApplication(updateApplicationDto);

        var getMyApplication = new GetMyApplicationDto(
                getDefaultFilter(),
                STUDENT_ENTITY.clientId()
        );
        var applications = applicationService.getMyApplications(getMyApplication);

        var updatedApplication = applications.stream()
                .filter(a -> a.applicationId().equals(applicationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));

        assertEquals(updateApplicationDto.startTime(), updatedApplication.startTime());
        assertEquals(updateApplicationDto.endTime(), updatedApplication.endTime());
    }

    @Test
    public void acceptApplication() {
        var timeSlot = TimeSlotEnum.SECOND;
        var year = 2010;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var applicationId = applicationService.createApplication(createApplicationDto);

        var acceptApplicationDto = new AcceptApplicationDto(
                DEANERY_ENTITY.clientId(),
                DEANERY_ENTITY.role(),
                applicationId
        );
        applicationService.acceptApplication(acceptApplicationDto);

        var getMyApplications = new GetMyApplicationDto(
                getDefaultFilter(),
                STUDENT_ENTITY.clientId()
        );
        var applications = applicationService.getMyApplications(getMyApplications);

        var acceptedApplication = applications.stream()
                .filter(a -> a.applicationId().equals(applicationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        assertEquals(ApplicationStatus.ACCEPTED, acceptedApplication.status());
    }

    @Test
    public void declineApplication() {
        var timeSlot = TimeSlotEnum.THIRD;
        var year = 2010;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var applicationId = applicationService.createApplication(createApplicationDto);

        var declineApplicationDto = new DeclineApplicationDto(
                DEANERY_ENTITY.clientId(),
                DEANERY_ENTITY.role(),
                applicationId
        );
        applicationService.declineApplication(declineApplicationDto);

        var getMyApplications = new GetMyApplicationDto(
                getDefaultFilter(),
                STUDENT_ENTITY.clientId()
        );
        var applications = applicationService.getMyApplications(getMyApplications);

        var declinedApplication = applications.stream()
                .filter(a -> a.applicationId().equals(applicationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        assertEquals(ApplicationStatus.DECLINED, declinedApplication.status());
    }

    @Test
    public void studentTryAcceptApplication() {
        var timeSlot = TimeSlotEnum.SECOND;
        var year = 2011;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var applicationId = applicationService.createApplication(createApplicationDto);

        var acceptApplicationDto = new AcceptApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                applicationId
        );

        assertThrows(ExceptionInApplication.class, () ->
                applicationService.acceptApplication(acceptApplicationDto)
        );
    }

    @Test
    public void studentTryDeclineApplication() {
        var timeSlot = TimeSlotEnum.THIRD;
        var year = 2011;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var applicationId = applicationService.createApplication(createApplicationDto);

        var declineApplicationDto = new DeclineApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                applicationId
        );

        assertThrows(ExceptionInApplication.class, () ->
                applicationService.declineApplication(declineApplicationDto)
        );
    }

    @Test
    public void tryDeleteReviewedApplication() {
        var timeSlot = TimeSlotEnum.SEVENTH;
        var year = 2012;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var applicationId = applicationService.createApplication(createApplicationDto);

        var acceptApplicationDto = new AcceptApplicationDto(
                DEANERY_ENTITY.clientId(),
                DEANERY_ENTITY.role(),
                applicationId
        );
        applicationService.acceptApplication(acceptApplicationDto);

        var deleteApplicationDto = new DeleteApplicationDto(STUDENT_ENTITY.clientId(), applicationId);
        assertThrows(ExceptionInApplication.class, () ->
                applicationService.deleteApplication(deleteApplicationDto)
        );
    }

    @Test
    public void tryUpdateReviewedApplication() {
        var timeSlot = TimeSlotEnum.FIRST;
        var year = 2012;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var applicationId = applicationService.createApplication(createApplicationDto);

        var acceptApplicationDto = new AcceptApplicationDto(
                DEANERY_ENTITY.clientId(),
                DEANERY_ENTITY.role(),
                applicationId
        );
        applicationService.acceptApplication(acceptApplicationDto);

        var updatedTimeSlot = TimeSlotEnum.THIRD;
        var updateApplicationDto = new UpdateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                applicationId,
                getStartTime(updatedTimeSlot, year),
                getEndTime(updatedTimeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        assertThrows(ExceptionInApplication.class, () ->
                applicationService.updateApplication(updateApplicationDto)
        );
    }

    @Test
    public void checkDeclineApplicationIfOneAccept() {
        var timeSlot = TimeSlotEnum.SEVENTH;
        var year = 2014;
        var firstCreateApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var firstApplicationId = applicationService.createApplication(firstCreateApplicationDto);

        var secondCreateApplicationDto = new CreateApplicationDto(
                TEACHER_ENTITY.clientId(),
                TEACHER_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var secondApplicationId = applicationService.createApplication(secondCreateApplicationDto);

        var acceptApplicationDto = new AcceptApplicationDto(
                DEANERY_ENTITY.clientId(),
                DEANERY_ENTITY.role(),
                firstApplicationId
        );
        applicationService.acceptApplication(acceptApplicationDto);

        var getAllApplicationDto = new GetAllApplicationsDto(
                getDefaultFilter(),
                DEANERY_ENTITY.clientId(),
                DEANERY_ENTITY.role()
        );
        var applications = applicationService.getAllApplications(getAllApplicationDto);

        var firstApplication = applications.stream()
                .filter(a -> a.applicationId().equals(firstApplicationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        var secondApplication = applications.stream()
                .filter(a -> a.applicationId().equals(secondApplicationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));

        assertEquals(ApplicationStatus.ACCEPTED, firstApplication.status());
        assertEquals(ApplicationStatus.DECLINED, secondApplication.status());
    }

    @Test
    public void createApplicationWithIncorrectStartTime() {
        var timeSlot = TimeSlotEnum.FIRST;
        var year = 2013;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                OffsetDateTime.of(year, 1, 4, 0, 0, 0, 0,ZoneOffset.UTC),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );

        assertThrows(ExceptionInApplication.class, () ->
                applicationService.createApplication(createApplicationDto)
        );
    }

    @Test
    public void createApplicationWithIncorrectEndTime() {
        var timeSlot = TimeSlotEnum.SECOND;
        var year = 2013;
        var createApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                OffsetDateTime.of(year, 1, 4, 0, 0, 0, 0, ZoneOffset.UTC),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );

        assertThrows(ExceptionInApplication.class, () ->
                applicationService.createApplication(createApplicationDto)
        );
    }

    @Test
    public void ifExistAcceptedTeacherApplicationDeclineStudent() {
        var timeSlot = TimeSlotEnum.FIRST;
        var year = 2017;
        var studentCreateApplicationDto = new CreateApplicationDto(
                STUDENT_ENTITY.clientId(),
                STUDENT_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var teacherCreateApplicationDto = new CreateApplicationDto(
                TEACHER_ENTITY.clientId(),
                TEACHER_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                false,
                null
        );
        var teacherApplicationId = applicationService.createApplication(teacherCreateApplicationDto);

        var acceptApplicationDto = new AcceptApplicationDto(
                DEANERY_ENTITY.clientId(),
                DEANERY_ENTITY.role(),
                teacherApplicationId
        );
        applicationService.acceptApplication(acceptApplicationDto);

        assertThrows(ExceptionInApplication.class, () ->
                applicationService.createApplication(studentCreateApplicationDto)
        );
    }

    @Test
    public void duplicateApplication() {
        var timeSlot = TimeSlotEnum.FOURTH;
        var year = 2019;
        var endTimeToDuplicate = getStartTime(timeSlot, year).plusDays(15);
        var createApplicationDto = new CreateApplicationDto(
                TEACHER_ENTITY.clientId(),
                TEACHER_ENTITY.role(),
                getStartTime(timeSlot, year),
                getEndTime(timeSlot, year),
                BUILD_ID,
                ROOM_ID,
                ClientRole.STUDENT.name(),
                true,
                endTimeToDuplicate
        );
        var applicationId = applicationService.createApplication(createApplicationDto);

        var acceptApplicationDto = new AcceptApplicationDto(
                DEANERY_ENTITY.clientId(),
                DEANERY_ENTITY.role(),
                applicationId
        );
        applicationService.acceptApplication(acceptApplicationDto);

        var filterDto = new ApplicationFilterDto(
                ApplicationStatus.ACCEPTED,
                getStartTime(timeSlot, year),
                endTimeToDuplicate,
                BUILD_ID,
                ROOM_ID
        );
        var getMyApplication = new GetMyApplicationDto(
                filterDto,
                TEACHER_ENTITY.clientId()
        );
        var applications = applicationService.getMyApplications(getMyApplication);

        assertEquals(3, applications.size());
    }

    private static void migrate() throws SQLException, LiquibaseException {
        var connection = DataSourceBuilder.create()
                .url(database.getJdbcUrl())
                .username(database.getUsername())
                .password(database.getPassword())
                .build()
                .getConnection();

        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));

        Liquibase liquibase = new Liquibase(CHANGELOG_FILE_PATH, new ClassLoaderResourceAccessor(), database);

        liquibase.update();
    }

    private static void databasePreparation() {
        addClientToDB(STUDENT_ENTITY);
        addClientToDB(TEACHER_ENTITY);
        addClientToDB(DEANERY_ENTITY);
        addAccommodationToDB(BUILD_ID, ROOM_ID);
    }

    private static ClientEntity formatClient(UUID clientId, Set<ClientRole> clientRoles) {
        return new ClientEntity(
                clientId,
                "Gordey",
                UUID.randomUUID().toString(),
                "1",
                "MALE",
                OffsetDateTime.now(),
                clientRoles
        );
    }

    private static void addClientToDB(ClientEntity entity) {
        dslContext.insertInto(CLIENT)
                .set(CLIENT.CLIENT_ID, entity.clientId())
                .set(CLIENT.CREATED_DATE, entity.createdDate())
                .set(CLIENT.GENDER, entity.gender())
                .set(CLIENT.NAME, entity.name())
                .set(CLIENT.PASSWORD, entity.password())
                .set(CLIENT.EMAIL, entity.email())
                .execute();

        entity.role().forEach(c ->
                dslContext.insertInto(ROLE)
                .set(ROLE.CLIENT_ID, entity.clientId())
                .set(ROLE.ROLE_, c.name())
                .execute()
        );
    }

    private static void addAccommodationToDB(int buildId, int roomId) {
        dslContext.insertInto(STUDYROOM)
                .set(STUDYROOM.ROOM, roomId)
                .set(STUDYROOM.BUILD, buildId)
                .execute();
    }

    private static OffsetDateTime getStartTime(TimeSlotEnum slot, int year) {
        return OffsetDateTime.of(year, 1, 4, slot.startHour, slot.startMinute, 0, 0, ZoneOffset.UTC);
    }

    private static OffsetDateTime getEndTime(TimeSlotEnum slot, int year) {
        return OffsetDateTime.of(year, 1, 4, slot.endHour, slot.endMinute, 0, 0, ZoneOffset.UTC);
    }

    private static ApplicationFilterDto getDefaultFilter() {
        return new ApplicationFilterDto(null, null, null, null, null);
    }
}
