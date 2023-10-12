package com.socialmedia.accounts.adapter.out.database;

import com.socialmedia.accounts.domain.User;

import java.time.Instant;
import java.util.UUID;

public class UserMapper {
    public User mapToUserEntity(
            UUID userId,
            String email,
            String hashedPassword,
            boolean verified,
            Long roleId,
            Instant creationTime
    ) {
        return new User(
                userId,
                email,
                hashedPassword,
                verified,
                roleId,
                creationTime
        );
    }
}
