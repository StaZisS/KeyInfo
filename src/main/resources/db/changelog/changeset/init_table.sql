-- liquibase formatted sql

-- changeset gordey_dovydenko:1
SET TIME ZONE 'UTC';

CREATE
    EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Client
(
    client_id    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name         VARCHAR(60)              NOT NULL,
    email        VARCHAR(60)              NOT NULL UNIQUE,
    password     VARCHAR(300)             NOT NULL,
    gender       VARCHAR(30)              NOT NULL CHECK ( gender IN ('MALE', 'FEMALE', 'UNSPECIFIED') ),
    created_date timestamp with time zone NOT NULL
);

-- rollback DROP TABLE Cart;

-- changeset gordey_dovydenko:2
CREATE TABLE Role
(
    client_id UUID,
    role      VARCHAR(30) NOT NULL CHECK ( role IN ('STUDENT', 'TEACHER', 'DEANERY', 'ADMIN', 'UNSPECIFIED') ),
    FOREIGN KEY (client_id) REFERENCES Client (client_id),
    PRIMARY KEY (client_id, role)
);
-- rollback DROP TABLE Role;

-- changeset gordey_dovydenko:3
CREATE TABLE StudyRoom
(
    room  INTEGER,
    build INTEGER,
    PRIMARY KEY (room, build)
);
-- rollback DROP TABLE StudyRoom;

-- changeset gordey_dovydenko:4
CREATE TABLE TimeSlot
(
    start_time TIMESTAMP WITH TIME ZONE,
    end_time   TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (start_time, end_time)
);
-- rollback DROP TABLE TimeSlot;

-- changeset gordey_dovydenko:5
CREATE TABLE Lesson
(
    lesson_id  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    teacher_id UUID                     NOT NULL,
    room       INTEGER                  NOT NULL,
    build      INTEGER                  NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time   TIMESTAMP WITH TIME ZONE NOT NULL,
    name       VARCHAR(60)              NOT NULL,
    FOREIGN KEY (room, build) REFERENCES StudyRoom (room, build),
    FOREIGN KEY (teacher_id) REFERENCES Client (client_id),
    FOREIGN KEY (start_time, end_time) REFERENCES TimeSlot (start_time, end_time)
);
-- rollback DROP TABLE Lesson;

-- changeset gordey_dovydenko:6
CREATE TABLE Request
(
    request_id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    request_creator    UUID                     NOT NULL,
    start_time         TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time           TIMESTAMP WITH TIME ZONE NOT NULL,
    status             VARCHAR(30)              NOT NULL CHECK ( status IN ('IN_PROCESS', 'ACCEPTED', 'DECLINED') ),
    created_time       TIMESTAMP WITH TIME ZONE NOT NULL,
    room               INTEGER                  NOT NULL,
    build              INTEGER                  NOT NULL,
    duplicate          BOOLEAN          DEFAULT FALSE,
    end_time_duplicate TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (start_time, end_time) REFERENCES TimeSlot (start_time, end_time),
    FOREIGN KEY (request_creator) REFERENCES Client (client_id),
    FOREIGN KEY (room, build) REFERENCES StudyRoom (room, build)
);
-- rollback DROP TABLE Request;

-- changeset gordey_dovydenko:7
CREATE TABLE Key
(
    key_id        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status        VARCHAR(30)              NOT NULL CHECK ( status IN ('IN_DEANERY', 'IN_HAND') ),
    key_holder_id UUID,
    room          INTEGER                  NOT NULL,
    build         INTEGER                  NOT NULL,
    last_access   TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (key_holder_id) REFERENCES Client (client_id),
    FOREIGN KEY (room, build) REFERENCES StudyRoom (room, build)
);
-- rollback DROP TABLE Key;

-- changeset gordey_dovydenko:8
CREATE TABLE TransferRequest
(
    transfer_request_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_id            UUID                     NOT NULL,
    receiver_id         UUID                     NOT NULL,
    status              VARCHAR(30)              NOT NULL CHECK ( status IN ('IN_PROCESS', 'ACCEPTED', 'DECLINED') ),
    created_time        TIMESTAMP WITH TIME ZONE NOT NULL,
    key_id              UUID                     NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES Client (client_id),
    FOREIGN KEY (receiver_id) REFERENCES Client (client_id),
    FOREIGN KEY (key_id) REFERENCES Key (key_id)
);
-- rollback DROP TABLE TransferRequest;

-- changeset T9404:9
INSERT INTO Client
VALUES ('8904ab0d-40e8-445d-9578-6254661e85b4', '1', '1',
        '$2a$10$p/SE0M1upWg1Szb3eGLn0.t4lLC5hbOpmL.VuY0CpTQYmiP8kXG/W',
        'MALE', '2024-02-18 05:00:28.746955 +00:00');
INSERT INTO Role
VALUES ('8904ab0d-40e8-445d-9578-6254661e85b4', 'ADMIN');

--changeset T9404:10
ALTER TABLE Request
ALTER COLUMN end_time_duplicate DROP NOT NULL;

--changeset gordey_dovydenko:11
INSERT INTO Client
VALUES ('04691fbd-0481-4fa5-a04f-e6ac76b21d9c', '1', '2',
        '$2a$10$p/SE0M1upWg1Szb3eGLn0.t4lLC5hbOpmL.VuY0CpTQYmiP8kXG/W',
        'MALE', '2024-02-18 05:00:28.746955 +00:00'),
       ('0d062136-8e97-4db9-9575-a2dea6273f06', '1', '3',
        '$2a$10$p/SE0M1upWg1Szb3eGLn0.t4lLC5hbOpmL.VuY0CpTQYmiP8kXG/W',
        'MALE', '2024-02-18 05:00:28.746955 +00:00'),
       ('38c482c6-93d9-443b-b954-64cea9ce9e10', '1', '4',
        '$2a$10$p/SE0M1upWg1Szb3eGLn0.t4lLC5hbOpmL.VuY0CpTQYmiP8kXG/W',
        'MALE', '2024-02-18 05:00:28.746955 +00:00'),
       ('e7a3b20c-bdae-423e-bfdd-b3fb3a3a7e2c', '1', '5',
        '$2a$10$p/SE0M1upWg1Szb3eGLn0.t4lLC5hbOpmL.VuY0CpTQYmiP8kXG/W',
        'MALE', '2024-02-18 05:00:28.746955 +00:00'),
       ('8936215c-c56e-4463-b7fb-b9520c1a90f4', '1', '6',
        '$2a$10$p/SE0M1upWg1Szb3eGLn0.t4lLC5hbOpmL.VuY0CpTQYmiP8kXG/W',
        'MALE', '2024-02-18 05:00:28.746955 +00:00');
INSERT INTO Role
VALUES ('04691fbd-0481-4fa5-a04f-e6ac76b21d9c', 'DEANERY'),
       ('0d062136-8e97-4db9-9575-a2dea6273f06', 'STUDENT'),
       ('38c482c6-93d9-443b-b954-64cea9ce9e10', 'TEACHER'),
       ('e7a3b20c-bdae-423e-bfdd-b3fb3a3a7e2c', 'UNSPECIFIED'),
       ('8936215c-c56e-4463-b7fb-b9520c1a90f4', 'STUDENT');

INSERT INTO StudyRoom
VALUES (1,1),
       (2,1),
       (3,1),
       (4,1),
       (1,2),
       (2,2),
       (1,3),
       (1,4),
       (2,4);

INSERT INTO Key
VALUES ('4f6f1a84-e5a4-4c92-a9c7-4827ab232d8c', 'IN_DEANERY', null, 1, 1, CURRENT_TIMESTAMP),
       ('584b8664-30ac-4b66-abf6-3714aa08c706', 'IN_DEANERY', null, 2, 1, CURRENT_TIMESTAMP),
       ('730d2912-aa9e-438e-ae04-07a7d6644c94', 'IN_HAND', '0d062136-8e97-4db9-9575-a2dea6273f06', 3, 1, CURRENT_TIMESTAMP),
       ('168a5adc-1163-478d-9df2-583695f51f6e', 'IN_HAND', '0d062136-8e97-4db9-9575-a2dea6273f06', 4, 1, CURRENT_TIMESTAMP),
       ('8f1a0ccd-be82-4dcc-a01e-fbaf371301a4', 'IN_HAND', '38c482c6-93d9-443b-b954-64cea9ce9e10', 1, 2, CURRENT_TIMESTAMP),
       ('4ce50263-3bac-479a-8265-8d2868fcf3c0', 'IN_HAND', '38c482c6-93d9-443b-b954-64cea9ce9e10', 2, 2, CURRENT_TIMESTAMP);

INSERT INTO TimeSlot
VALUES ('2024-02-18 08:45:00.0 +00:00', '2024-02-18 10:20:00.0 +00:00'),
       ('2024-02-18 10:35:00.0 +00:00', '2024-02-18 12:10:00.0 +00:00'),
       ('2024-02-18 12:25:00.0 +00:00', '2024-02-18 14:00:00.0 +00:00'),
       ('2024-02-18 14:45:00.0 +00:00', '2024-02-18 16:20:00.0 +00:00'),
       ('2024-02-18 16:35:00.0 +00:00', '2024-02-18 18:10:00.0 +00:00'),
       ('2024-02-18 18:25:00.0 +00:00', '2024-02-18 20:00:00.0 +00:00'),
       ('2024-02-18 20:15:00.0 +00:00', '2024-02-18 21:50:00.0 +00:00'),

       ('2024-02-19 08:45:00.0 +00:00', '2024-02-19 10:20:00.0 +00:00'),
       ('2024-02-19 10:35:00.0 +00:00', '2024-02-19 12:10:00.0 +00:00'),
       ('2024-02-19 12:25:00.0 +00:00', '2024-02-19 14:00:00.0 +00:00'),
       ('2024-02-19 14:45:00.0 +00:00', '2024-02-19 16:20:00.0 +00:00'),
       ('2024-02-19 16:35:00.0 +00:00', '2024-02-19 18:10:00.0 +00:00'),
       ('2024-02-19 18:25:00.0 +00:00', '2024-02-19 20:00:00.0 +00:00'),
       ('2024-02-19 20:15:00.0 +00:00', '2024-02-19 21:50:00.0 +00:00'),

       ('2024-02-20 08:45:00.0 +00:00', '2024-02-20 10:20:00.0 +00:00'),
       ('2024-02-20 10:35:00.0 +00:00', '2024-02-20 12:10:00.0 +00:00'),
       ('2024-02-20 12:25:00.0 +00:00', '2024-02-20 14:00:00.0 +00:00'),
       ('2024-02-20 14:45:00.0 +00:00', '2024-02-20 16:20:00.0 +00:00'),
       ('2024-02-20 16:35:00.0 +00:00', '2024-02-20 18:10:00.0 +00:00'),
       ('2024-02-20 18:25:00.0 +00:00', '2024-02-20 20:00:00.0 +00:00'),
       ('2024-02-20 20:15:00.0 +00:00', '2024-02-20 21:50:00.0 +00:00'),

       ('2024-02-21 08:45:00.0 +00:00', '2024-02-21 10:20:00.0 +00:00'),
       ('2024-02-21 10:35:00.0 +00:00', '2024-02-21 12:10:00.0 +00:00'),
       ('2024-02-21 12:25:00.0 +00:00', '2024-02-21 14:00:00.0 +00:00'),
       ('2024-02-21 14:45:00.0 +00:00', '2024-02-21 16:20:00.0 +00:00'),
       ('2024-02-21 16:35:00.0 +00:00', '2024-02-21 18:10:00.0 +00:00'),
       ('2024-02-21 18:25:00.0 +00:00', '2024-02-21 20:00:00.0 +00:00'),
       ('2024-02-21 20:15:00.0 +00:00', '2024-02-21 21:50:00.0 +00:00'),

       ('2024-02-22 08:45:00.0 +00:00', '2024-02-22 10:20:00.0 +00:00'),
       ('2024-02-22 10:35:00.0 +00:00', '2024-02-22 12:10:00.0 +00:00'),
       ('2024-02-22 12:25:00.0 +00:00', '2024-02-22 14:00:00.0 +00:00'),
       ('2024-02-22 14:45:00.0 +00:00', '2024-02-22 16:20:00.0 +00:00'),
       ('2024-02-22 16:35:00.0 +00:00', '2024-02-22 18:10:00.0 +00:00'),
       ('2024-02-22 18:25:00.0 +00:00', '2024-02-22 20:00:00.0 +00:00'),
       ('2024-02-22 20:15:00.0 +00:00', '2024-02-22 21:50:00.0 +00:00'),

       ('2024-02-23 08:45:00.0 +00:00', '2024-02-23 10:20:00.0 +00:00'),
       ('2024-02-23 10:35:00.0 +00:00', '2024-02-23 12:10:00.0 +00:00'),
       ('2024-02-23 12:25:00.0 +00:00', '2024-02-23 14:00:00.0 +00:00'),
       ('2024-02-23 14:45:00.0 +00:00', '2024-02-23 16:20:00.0 +00:00'),
       ('2024-02-23 16:35:00.0 +00:00', '2024-02-23 18:10:00.0 +00:00'),
       ('2024-02-23 18:25:00.0 +00:00', '2024-02-23 20:00:00.0 +00:00'),
       ('2024-02-23 20:15:00.0 +00:00', '2024-02-23 21:50:00.0 +00:00'),

       ('2024-02-24 08:45:00.0 +00:00', '2024-02-24 10:20:00.0 +00:00'),
       ('2024-02-24 10:35:00.0 +00:00', '2024-02-24 12:10:00.0 +00:00'),
       ('2024-02-24 12:25:00.0 +00:00', '2024-02-24 14:00:00.0 +00:00'),
       ('2024-02-24 14:45:00.0 +00:00', '2024-02-24 16:20:00.0 +00:00'),
       ('2024-02-24 16:35:00.0 +00:00', '2024-02-24 18:10:00.0 +00:00'),
       ('2024-02-24 18:25:00.0 +00:00', '2024-02-24 20:00:00.0 +00:00'),
       ('2024-02-24 20:15:00.0 +00:00', '2024-02-24 21:50:00.0 +00:00');

INSERT INTO Request
VALUES ('c308ebf6-adf9-472f-89fe-7305fc6a441e', '8936215c-c56e-4463-b7fb-b9520c1a90f4', '2024-02-23 08:45:00.0 +00:00', '2024-02-23 10:20:00.0 +00:00', 'IN_PROCESS', CURRENT_TIMESTAMP, 1, 1, false, null),
       ('da9647a1-f179-4895-94ec-fecd8b282f46', '8936215c-c56e-4463-b7fb-b9520c1a90f4', '2024-02-23 10:35:00.0 +00:00', '2024-02-23 12:10:00.0 +00:00', 'IN_PROCESS', CURRENT_TIMESTAMP, 2, 1, false, null),
       ('ac286cfb-db93-4471-9def-63252281c410', '8936215c-c56e-4463-b7fb-b9520c1a90f4', '2024-02-23 12:25:00.0 +00:00', '2024-02-23 14:00:00.0 +00:00', 'IN_PROCESS', CURRENT_TIMESTAMP, 1, 1, false, null),
       ('d5baeea2-4ca3-4439-98d6-6f7871ff4506', '8936215c-c56e-4463-b7fb-b9520c1a90f4', '2024-02-23 14:45:00.0 +00:00', '2024-02-23 16:20:00.0 +00:00', 'IN_PROCESS', CURRENT_TIMESTAMP, 3, 1, false, null),
       ('9b9d828e-1631-426f-b729-29fefe461ac0', '8936215c-c56e-4463-b7fb-b9520c1a90f4', '2024-02-23 16:35:00.0 +00:00', '2024-02-23 18:10:00.0 +00:00', 'IN_PROCESS', CURRENT_TIMESTAMP, 4, 1, false, null),
       ('6b264d5d-c687-4a04-9f89-d32e172ee5dc', '8936215c-c56e-4463-b7fb-b9520c1a90f4', '2024-02-23 18:25:00.0 +00:00', '2024-02-23 20:00:00.0 +00:00', 'IN_PROCESS', CURRENT_TIMESTAMP, 1, 2, false, null),
       ('76a3f0d0-340e-4704-ab8a-ff35c03c6b02', '8936215c-c56e-4463-b7fb-b9520c1a90f4', '2024-02-23 20:15:00.0 +00:00', '2024-02-23 21:50:00.0 +00:00', 'IN_PROCESS', CURRENT_TIMESTAMP, 1, 2, false, null),
       ('8df39b16-0947-4ce5-ba09-7d59a957deb2', '8936215c-c56e-4463-b7fb-b9520c1a90f4', '2024-02-24 08:45:00.0 +00:00', '2024-02-24 10:20:00.0 +00:00', 'IN_PROCESS', CURRENT_TIMESTAMP, 2, 2, false, null),
       ('493e677c-c5eb-4c75-aa6e-398a61f06f09', '8936215c-c56e-4463-b7fb-b9520c1a90f4', '2024-02-24 10:35:00.0 +00:00', '2024-02-24 12:10:00.0 +00:00', 'IN_PROCESS', CURRENT_TIMESTAMP, 1, 3, false, null),
       ('1a5c46f8-07e1-442c-9f15-dd7dc52843f9', '8936215c-c56e-4463-b7fb-b9520c1a90f4', '2024-02-24 12:25:00.0 +00:00', '2024-02-24 14:00:00.0 +00:00', 'IN_PROCESS', CURRENT_TIMESTAMP, 1, 4, false, null);
