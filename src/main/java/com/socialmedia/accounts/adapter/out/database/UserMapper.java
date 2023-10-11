package com.socialmedia.accounts.adapter.out.database;

import com.socialmedia.accounts.domain.User;

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
