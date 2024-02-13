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
    role      VARCHAR(30) NOT NULL CHECK ( role IN ('STUDENT', 'TEACHER', 'DEANERY', 'ADMIN') ),
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
    request_id      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    request_creator UUID                     NOT NULL,
    start_time      TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time        TIMESTAMP WITH TIME ZONE NOT NULL,
    status          VARCHAR(30)              NOT NULL CHECK ( status IN ('IN_PROCESS', 'ACCEPTED', 'DECLINED') ),
    created_time    TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (start_time, end_time) REFERENCES TimeSlot (start_time, end_time),
    FOREIGN KEY (request_creator) REFERENCES Client (client_id)
);
-- rollback DROP TABLE Request;

-- changeset gordey_dovydenko:7
CREATE TABLE Key
(
    key_id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status                  VARCHAR(30)              NOT NULL CHECK ( status IN ('IN_DEANERY', 'IN_HAND') ),
    key_holder_id           UUID,
    room                    INTEGER                  NOT NULL,
    build                   INTEGER                  NOT NULL,
    last_status_time_update TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (key_holder_id) REFERENCES Client (client_id),
    FOREIGN KEY (room, build) REFERENCES StudyRoom (room, build)
);
-- rollback DROP TABLE Key;
