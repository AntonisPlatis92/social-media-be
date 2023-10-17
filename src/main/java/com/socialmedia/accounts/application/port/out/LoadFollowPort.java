package com.socialmedia.accounts.application.port.out;

import com.socialmedia.accounts.domain.Follow;

import java.util.Optional;
import java.util.UUID;

public interface LoadFollowPort {
    Optional<Follow> loadFollowByPk(UUID followerId, UUID followingId);
}
