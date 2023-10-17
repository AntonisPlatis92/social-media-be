package com.socialmedia.accounts.domain;

import com.socialmedia.accounts.domain.commands.CreateFollowCommand;
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

    public static Follow createFollowFromCommand(CreateFollowCommand command) {
        return new Follow(
                command.followerId(),
                command.followingId(),
                Instant.now(ClockConfig.utcClock())
        );
    }
}
