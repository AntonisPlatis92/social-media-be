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

create table roles
(
    id                   bigint                              not null
        primary key,
    role_name            varchar(255),
    has_post_chars_limit boolean   default true,
    post_chars_limit     bigint,
    has_comments_limit   boolean   default true,
    comments_limit       bigint,
    creation_time        timestamp default CURRENT_TIMESTAMP not null
);

INSERT INTO roles (id, role_name, has_post_chars_limit, post_chars_limit, has_comments_limit, comments_limit,
                          creation_time)
VALUES (1, 'FREE', true, 1000, true, 5, DEFAULT);

INSERT INTO roles (id, role_name, has_post_chars_limit, post_chars_limit, has_comments_limit, comments_limit,
                          creation_time)
VALUES (2, 'PREMIUM', true, 3000, false, null, DEFAULT);

create table posts
(
    id              uuid                                not null
        primary key,
    user_id         uuid                                not null,
    body            text                                not null,
    creation_time   timestamp default CURRENT_TIMESTAMP not null,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

create table comments
(
    id              uuid                                not null
        primary key,
    post_id         uuid                                not null,
    user_id         uuid                                not null,
    body            text                                not null,
    creation_time   timestamp default CURRENT_TIMESTAMP not null,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES posts (id)
);