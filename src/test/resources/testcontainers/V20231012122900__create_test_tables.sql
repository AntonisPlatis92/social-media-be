create table users
(
    id              uuid                                not null
        primary key,
    email           varchar(255)                        not null,
    hashed_password varchar(255)                        not null,
    verified        boolean   default false             not null,
    role_id         bigint                              not null,
    creation_time   timestamp default CURRENT_TIMESTAMP not null
);