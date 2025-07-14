-- liquibase formatted sql

-- changeset lab7:1749534979371-1
CREATE TABLE task_notification
(
    id        BIGSERIAL PRIMARY KEY NOT NULL,
    task_id   BIGINT,
    title     VARCHAR(255),
    status    VARCHAR(255),
    timestamp TIMESTAMP WITHOUT TIME ZONE
);

