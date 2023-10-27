package com.socialmedia.accounts.adapter.out.mappers;

import com.socialmedia.accounts.adapter.out.jpa.UserJpa;
import com.socialmedia.accounts.domain.User;

public class UserEntityJpaMapper {
    public static UserJpa mapFromUserToUserJpaEntity(User user) {
        return new UserJpa(
                user.getUserId(),
                user.getEmail(),
                user.getHashedPassword(),
                user.isVerified(),
                user.getRole().getId(),
                user.getCreationTime()
        );
    }
}
