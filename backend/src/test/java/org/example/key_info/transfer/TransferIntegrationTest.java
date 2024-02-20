package org.example.key_info.transfer;

import com.github.dockerjava.api.exception.NotFoundException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.key_info.core.client.repository.ClientEntity;
import org.example.key_info.core.client.repository.ClientRole;
import org.example.key_info.core.key.repository.KeyEntity;
import org.example.key_info.core.key.repository.KeyRepository;
import org.example.key_info.core.key.repository.KeyStatus;
import org.example.key_info.core.transfer.TransferService;
import org.example.key_info.core.transfer.TransferStatus;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.transfer.AcceptTransferDto;
import org.example.key_info.public_interface.transfer.CreateTransferDto;
import org.example.key_info.public_interface.transfer.DeclineTransferDto;
import org.example.key_info.public_interface.transfer.DeleteTransferDto;
import org.example.key_info.public_interface.transfer.GetForeignTransfersDto;
import org.example.key_info.public_interface.transfer.GetMyTransfersDto;
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
import java.util.Set;
import java.util.UUID;

import static com.example.shop.public_.tables.Client.CLIENT;
import static com.example.shop.public_.tables.Role.ROLE;
import static com.example.shop.public_.tables.Studyroom.STUDYROOM;
import static com.example.shop.public_.tables.Key.KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@SpringJUnitConfig(classes = {TransferIntegrationTestConfiguration.class})
@ActiveProfiles("test")
public class TransferIntegrationTest {
    private static final String CHANGELOG_FILE_PATH = "db/changelog/db.changelog-master.yaml";

    private static final ClientEntity FIRST_STUDENT = formatClient(UUID.randomUUID(), Set.of(ClientRole.STUDENT));
    private static final ClientEntity SECOND_STUDENT = formatClient(UUID.randomUUID(), Set.of(ClientRole.STUDENT));
    private static final ClientEntity TEACHER_ENTITY = formatClient(UUID.randomUUID(), Set.of(ClientRole.TEACHER));
    private static final ClientEntity DEANERY_ENTITY = formatClient(UUID.randomUUID(), Set.of(ClientRole.DEANERY));

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

    @Autowired private TransferService transferService;
    @Autowired private KeyRepository keyRepository;

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
    public void createTransfer() {
        var build = 1;
        var room = 1;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var getMyTransfersDtoFirstStudent = new GetMyTransfersDto(FIRST_STUDENT.clientId(), TransferStatus.IN_PROCESS.name());
        var getForeignTransfersDtoSecondStudent = new GetForeignTransfersDto(SECOND_STUDENT.clientId(), TransferStatus.IN_PROCESS.name());

        var transfersFirstStudent = transferService.getMyTransfers(getMyTransfersDtoFirstStudent);
        var transfersSecondStudent = transferService.getForeignTransfer(getForeignTransfersDtoSecondStudent);

        var transferFirstStudent = transfersFirstStudent.stream()
                .filter(t -> t.transferId().equals(transferId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        var transferSecondStudent = transfersSecondStudent.stream()
                .filter(t -> t.transferId().equals(transferId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));

        assertEquals(FIRST_STUDENT.clientId(), transferFirstStudent.ownerId());
        assertEquals(SECOND_STUDENT.clientId(), transferSecondStudent.receiverId());
    }

    @Test
    public void deleteMyTransfer() {
        var build = 2;
        var room = 2;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var deleteTransferDto = new DeleteTransferDto(FIRST_STUDENT.clientId(), transferId);
        transferService.deleteMyTransfer(deleteTransferDto);

        var getMyTransfersDtoFirstStudent = new GetMyTransfersDto(FIRST_STUDENT.clientId(), TransferStatus.IN_PROCESS.name());
        var transfersFirstStudent = transferService.getMyTransfers(getMyTransfersDtoFirstStudent);
        var transferFirstStudent = transfersFirstStudent.stream()
                .filter(t -> t.transferId().equals(transferId))
                .findFirst();

        assertTrue(transferFirstStudent.isEmpty());
    }

    @Test
    public void tryDeleteForeignTransfer() {
        var build = 3;
        var room = 3;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var deleteTransferDto = new DeleteTransferDto(SECOND_STUDENT.clientId(), transferId);
        assertThrows(ExceptionInApplication.class, () ->
                transferService.deleteMyTransfer(deleteTransferDto)
        );
    }

    @Test
    public void tryDeleteReviewedTransfer() {
        var build = 4;
        var room = 4;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var acceptTransferDto = new AcceptTransferDto(SECOND_STUDENT.clientId(), transferId);
        transferService.acceptTransfer(acceptTransferDto);

        var deleteTransferDto = new DeleteTransferDto(FIRST_STUDENT.clientId(), transferId);
        assertThrows(ExceptionInApplication.class, () ->
                transferService.deleteMyTransfer(deleteTransferDto)
        );
    }

    @Test
    public void acceptTransfer() {
        var build = 5;
        var room = 5;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var acceptTransferDto = new AcceptTransferDto(SECOND_STUDENT.clientId(), transferId);
        transferService.acceptTransfer(acceptTransferDto);

        var getMyTransfersDtoFirstStudent = new GetMyTransfersDto(FIRST_STUDENT.clientId(), TransferStatus.ACCEPTED.name());
        var transfers = transferService.getMyTransfers(getMyTransfersDtoFirstStudent);
        var transfer = transfers.stream()
                .filter(t -> t.transferId().equals(transferId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));

        assertEquals(TransferStatus.ACCEPTED, transfer.status());
    }

    @Test
    public void declineTransfer() {
        var build = 6;
        var room = 6;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var declineTransferDto = new DeclineTransferDto(SECOND_STUDENT.clientId(), transferId);
        transferService.declineTransfer(declineTransferDto);

        var getMyTransfersDtoFirstStudent = new GetMyTransfersDto(FIRST_STUDENT.clientId(), TransferStatus.DECLINED.name());
        var transfers = transferService.getMyTransfers(getMyTransfersDtoFirstStudent);
        var transfer = transfers.stream()
                .filter(t -> t.transferId().equals(transferId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));

        assertEquals(TransferStatus.DECLINED, transfer.status());
    }

    @Test
    public void tryAcceptReviewedTransfer() {
        var build = 7;
        var room = 7;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var acceptTransferDto = new AcceptTransferDto(SECOND_STUDENT.clientId(), transferId);
        transferService.acceptTransfer(acceptTransferDto);
        assertThrows(ExceptionInApplication.class, () ->
                transferService.acceptTransfer(acceptTransferDto)
        );
    }

    @Test
    public void tryDeclineReviewedTransfer() {
        var build = 8;
        var room = 8;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var declineTransferDto = new DeclineTransferDto(SECOND_STUDENT.clientId(), transferId);
        transferService.declineTransfer(declineTransferDto);
        assertThrows(ExceptionInApplication.class, () ->
                transferService.declineTransfer(declineTransferDto)
        );
    }

    @Test
    public void tryAcceptOwnTransfer() {
        var build = 9;
        var room = 9;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var acceptTransferDto = new AcceptTransferDto(FIRST_STUDENT.clientId(), transferId);
        assertThrows(ExceptionInApplication.class, () ->
                transferService.acceptTransfer(acceptTransferDto)
        );
    }

    @Test
    public void tryDeclineOwnTransfer() {
        var build = 10;
        var room = 10;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var declineTransferDto = new DeclineTransferDto(FIRST_STUDENT.clientId(), transferId);
        assertThrows(ExceptionInApplication.class, () ->
                transferService.declineTransfer(declineTransferDto)
        );
    }

    @Test
    public void checkDeclinedAllTransferIfOneAccept() {
        var build = 11;
        var room = 11;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var firstCreateTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var secondCreateTransferDto = new CreateTransferDto(
                TEACHER_ENTITY.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );

        var firstTransferId = transferService.createTransfer(firstCreateTransferDto);
        var secondTransferId = transferService.createTransfer(secondCreateTransferDto);

        var acceptTransferDto = new AcceptTransferDto(SECOND_STUDENT.clientId(), secondTransferId);
        transferService.acceptTransfer(acceptTransferDto);

        var getMyTransfersDtoAccepted = new GetMyTransfersDto(TEACHER_ENTITY.clientId(), TransferStatus.ACCEPTED.name());
        var getMyTransfersDtoDeclined = new GetMyTransfersDto(FIRST_STUDENT.clientId(), TransferStatus.DECLINED.name());

        var acceptedTransfers = transferService.getMyTransfers(getMyTransfersDtoAccepted);
        var declinedTransfers = transferService.getMyTransfers(getMyTransfersDtoDeclined);

        var acceptTransfer = acceptedTransfers.stream()
                .filter(t -> t.transferId().equals(secondTransferId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        var declineTransfer = declinedTransfers.stream()
                .filter(t -> t.transferId().equals(firstTransferId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));

        assertEquals(TransferStatus.ACCEPTED, acceptTransfer.status());
        assertEquals(TransferStatus.DECLINED, declineTransfer.status());
    }

    @Test
    public void checkUpdatedKeyHolderAfterAccept() {
        var build = 12;
        var room = 12;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var acceptTransferDto = new AcceptTransferDto(SECOND_STUDENT.clientId(), transferId);
        transferService.acceptTransfer(acceptTransferDto);

        var secondStudentKeys = keyRepository.getMyKeys(SECOND_STUDENT.clientId());
        var keyAfterAccept = secondStudentKeys.stream()
                .filter(k -> k.buildId() == build && k.roomId() == room)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Ключ не найден"));

        assertEquals(KeyStatus.IN_HAND, keyAfterAccept.status());
        assertEquals(SECOND_STUDENT.clientId(), keyAfterAccept.keyHolderId());
    }

    @Test
    public void checkNotUpdateKeyHolderAfterDecline() {
        var build = 13;
        var room = 13;
        addAccommodationToDB(build, room);
        var key = formatKey(KeyStatus.IN_HAND, FIRST_STUDENT.clientId(), build, room);
        addKeyToDB(key);
        var createTransferDto = new CreateTransferDto(
                FIRST_STUDENT.clientId(),
                SECOND_STUDENT.clientId(),
                key.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        var declineTransferDto = new DeclineTransferDto(SECOND_STUDENT.clientId(), transferId);
        transferService.declineTransfer(declineTransferDto);

        var secondStudentKeys = keyRepository.getMyKeys(SECOND_STUDENT.clientId());
        var keyAfterDeclineSecondStudent = secondStudentKeys.stream()
                .filter(k -> k.buildId() == build && k.roomId() == room)
                .findFirst();
        assertTrue(keyAfterDeclineSecondStudent.isEmpty());

        var firstStudentKeys = keyRepository.getMyKeys(FIRST_STUDENT.clientId());
        var keyAfterDeclineFirstStudent = firstStudentKeys.stream()
                .filter(k -> k.buildId() == build && k.roomId() == room)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Ключ не найден"));
        assertEquals(KeyStatus.IN_HAND, keyAfterDeclineFirstStudent.status());
        assertEquals(FIRST_STUDENT.clientId(), keyAfterDeclineFirstStudent.keyHolderId());
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
        addClientToDB(FIRST_STUDENT);
        addClientToDB(SECOND_STUDENT);
        addClientToDB(TEACHER_ENTITY);
        addClientToDB(DEANERY_ENTITY);
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

    private static KeyEntity formatKey(KeyStatus status, UUID holderId, int buildId, int roomId) {
        return new KeyEntity(
                UUID.randomUUID(),
                status,
                holderId,
                roomId,
                buildId,
                OffsetDateTime.now()
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

    private static void addKeyToDB(KeyEntity entity) {
        dslContext.insertInto(KEY)
                .set(KEY.KEY_ID, entity.keyId())
                .set(KEY.STATUS, entity.status().name())
                .set(KEY.KEY_HOLDER_ID, entity.keyHolderId())
                .set(KEY.ROOM, entity.roomId())
                .set(KEY.BUILD, entity.buildId())
                .set(KEY.LAST_ACCESS, entity.lastAccess())
                .execute();
    }
}
