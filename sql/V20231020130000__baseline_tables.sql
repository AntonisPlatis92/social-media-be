create table users
(
    id              uuid                                not null
        primary key,
    email           varchar(255)                        not null,
    hashed_password varchar(255)                        not null,
    verified        boolean   default false             not null,
    role_id         bigint                              not null,
    creation_time   timestamp default CURRENT_TIMESTAMP not null,
    constraint unique_user_email unique (email)
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
    id            uuid                                not null
        primary key,
    user_id       uuid                                not null,
    body          text                                not null,
    creation_time timestamp default CURRENT_TIMESTAMP not null,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

create table comments
(
    id            uuid                                not null
        primary key,
    post_id       uuid                                not null,
    user_id       uuid                                not null,
    body          text                                not null,
    creation_time timestamp default CURRENT_TIMESTAMP not null,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

create table follows
(
    follower_id   uuid                                not null,
    following_id  uuid                                not null,
    creation_time timestamp default CURRENT_TIMESTAMP not null,
    primary key (follower_id, following_id),
    FOREIGN KEY (follower_id) REFERENCES users (id),
    FOREIGN KEY (following_id) REFERENCES users (id)
);

create view following_posts(user_id, post_id, post_user_email, post_body, post_creation_time) as
SELECT temp_following_posts.user_id,
       temp_following_posts.post_id,
       u.email AS post_user_email,
       temp_following_posts.post_body,
       temp_following_posts.post_creation_time
FROM users u
         JOIN (SELECT u_1.id          AS user_id,
                      p.id            AS post_id,
                      p.user_id       AS post_user_id,
                      p.body          AS post_body,
                      p.creation_time AS post_creation_time
               FROM follows f
                        JOIN users u_1 ON f.follower_id = u_1.id
                        JOIN posts p ON p.user_id = f.following_id) temp_following_posts
              ON temp_following_posts.post_user_id = u.id
ORDER BY temp_following_posts.post_creation_time DESC;

CREATE OR REPLACE VIEW comments_on_own_posts AS
SELECT user_id, post_id, u.email comment_user_email, comment_body, comment_creation_time
FROM users u
         JOIN
     (SELECT p.user_id       user_id,
             p.id            post_id,
             c.user_id       comment_user_id,
             c.body          comment_body,
             c.creation_time comment_creation_time
      FROM posts p
               JOIN comments c ON p.id = c.post_id) temp_own_comments on comment_user_id = u.id
ORDER BY comment_creation_time desc;

create view comments_on_own_and_following_posts
            (user_id, post_id, comment_user_email, comment_body, comment_creation_time) as
SELECT temp_following_comments.user_id,
       temp_following_comments.post_id,
       u.email AS comment_user_email,
       temp_following_comments.comment_body,
       temp_following_comments.comment_creation_time
FROM users u
         JOIN (SELECT u_1.id          AS user_id,
                      p.id            AS post_id,
                      c.user_id       AS comment_user_id,
                      c.body          AS comment_body,
                      c.creation_time AS comment_creation_time
               FROM follows f
                        JOIN users u_1 ON f.follower_id = u_1.id
                        JOIN posts p ON p.user_id = f.following_id
                        JOIN comments c ON p.id = c.post_id
               UNION
               SELECT u_1.id          AS user_id,
                      p.id            AS post_id,
                      c.user_id       AS comment_user_id,
                      c.body          AS comment_body,
                      c.creation_time AS comment_creation_time
               FROM users u_1
                        JOIN posts p ON p.user_id = u_1.id
                        JOIN comments c ON p.id = c.post_id) temp_following_comments
              ON temp_following_comments.comment_user_id = u.id
ORDER BY temp_following_comments.comment_creation_time DESC;