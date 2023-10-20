package com.socialmedia.accounts.application.port.out;

import com.socialmedia.accounts.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.accounts.domain.Follow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadFollowPort {
    Optional<Follow> loadFollowByPk(UUID followerId, UUID followingId);
    FollowsReturnVM loadFollowsByUserId(UUID userId);
}
