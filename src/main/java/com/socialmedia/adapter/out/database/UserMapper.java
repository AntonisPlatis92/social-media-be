package com.socialmedia.adapter.out.database;

import com.socialmedia.application.domain.entities.User;

import java.time.Instant;

public class UserMapper {
    public User mapToUserEntity(
            String email,
            String hashedPassword,
            boolean verified,
            Long roleId,
            Instant creationTime
    ) {
        return new User(
                email,
                hashedPassword,
                verified,
                roleId,
                creationTime
        );
    }
}
