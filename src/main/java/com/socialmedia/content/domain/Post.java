package com.socialmedia.content.domain;

import com.socialmedia.config.ClockConfig;
import com.socialmedia.content.domain.commands.CreatePostCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Post {
    private UUID id;
    private UUID userId;
    private String body;
    private Instant creationTime;

    public static Post createPostFromCommand(CreatePostCommand command) {
        return new Post(
                UUID.randomUUID(),
                command.userId(),
                command.body(),
                Instant.now(ClockConfig.utcClock())
        );
    }
}
