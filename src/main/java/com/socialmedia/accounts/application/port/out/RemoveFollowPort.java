package com.socialmedia.accounts.application.port.out;

import java.util.UUID;

public interface RemoveFollowPort {
    void removeFollow(UUID followerId, UUID followingId);
}