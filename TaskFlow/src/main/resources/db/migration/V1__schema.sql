CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    name       VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255),
    active     BOOLEAN NOT NULL
);

CREATE TABLE project
(
    id          BIGSERIAL PRIMARY KEY,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    name        VARCHAR(32),
    description TEXT,
    status      VARCHAR(16),
    owner_id    BIGINT,
    CONSTRAINT  FK_PROJECT_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE project_user
(
    id BIGSERIAL PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    CONSTRAINT fk_prouse_on_project FOREIGN KEY (project_id) REFERENCES project (id),
    CONSTRAINT fk_prouse_on_user FOREIGN KEY (member_id) REFERENCES users (id)
);

CREATE TABLE task
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    title       VARCHAR(128),
    description TEXT,
    status      VARCHAR(10),
    priority    VARCHAR(10),
    deadline    TIMESTAMP,
    project_id  BIGINT,
    user_id     BIGINT,
    CONSTRAINT  FK_TASK_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT  FK_TASK_ON_PROJECT FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE
);

CREATE TABLE comment
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    comment    TEXT,
    task_id    BIGINT,
    user_id    BIGINT,
    CONSTRAINT FK_COMMENT_ON_TASK FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE CASCADE,
    CONSTRAINT FK_COMMENT_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

