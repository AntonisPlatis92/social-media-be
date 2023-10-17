package com.socialmedia.follows.application.port.out;

import com.socialmedia.follows.domain.Follow;

import java.util.Optional;
import java.util.UUID;

public interface LoadFollowPort {
    Optional<Follow> loadFollowByPk(UUID followerId, UUID followingId);
}
