package com.socialmedia.accounts.domain;

import com.socialmedia.config.ClockConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Follow {
    private UUID followerId;
    private UUID followingId;
    private Instant creationTime;

    public static Follow createFollowFromFollowerIdAndFollowingId(
            UUID followerId,
            UUID followingId) {
        return new Follow(
                followerId,
                followingId,
                Instant.now(ClockConfig.utcClock())
        );
    }
}
