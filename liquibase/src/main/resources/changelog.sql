--liquibase formatted sql


--changeset liquibase:1
create table users
(
    id         bigint GENERATED ALWAYS AS IDENTITY,
    sub        varchar(100) not null,
    email      varchar(300) not null,
    created_at timestamp    not null,
    updated_at timestamp    not null,
    PRIMARY KEY (id),
    UNIQUE (sub)
);
--rollback drop table users;

--changeset liquibase:2
create table todo
(
    id          bigint GENERATED ALWAYS AS IDENTITY,
    title       varchar(100)             not null,
    description varchar(2000) null,
    due_date    timestamp with time zone not null,
    user_fk     bigint                   not null,
    PRIMARY KEY (id),
    FOREIGN KEY (user_fk) REFERENCES users (id)
);
--rollback drop table todo;