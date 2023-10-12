create table users
(
    email           varchar(255)                        not null
        primary key,
    hashed_password varchar(255)                        not null,
    verified        boolean   default false             not null,
    role_id         bigint                              not null,
    creation_time   timestamp default CURRENT_TIMESTAMP not null
);