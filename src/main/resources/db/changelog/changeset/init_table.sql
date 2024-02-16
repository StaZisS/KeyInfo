-- liquibase formatted sql

-- changeset gordey_dovydenko:1
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
    end_time_duplicate TIMESTAMP WITH TIME ZONE NOT NULL,
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
INSERT INTO Client VALUES
    ('8904ab0d-40e8-445d-9578-6254661e85b4', '1', '1', '$2a$10$p/SE0M1upWg1Szb3eGLn0.t4lLC5hbOpmL.VuY0CpTQYmiP8kXG/W',
     'MALE', '2024-02-16 05:00:28.746955 +00:00');

INSERT INTO Role VALUES
    ('8904ab0d-40e8-445d-9578-6254661e85b4', 'ADMIN');

--changeset T9404:10
ALTER TABLE Request
ALTER COLUMN end_time_duplicate DROP NOT NULL;