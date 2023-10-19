package com.socialmedia.posts.domain;

import com.socialmedia.config.ClockConfig;
import com.socialmedia.posts.domain.commands.CreateCommentCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Comment {
    private UUID id;
    private UUID userId;
    private UUID postId;
    private String body;
    private Instant creationTime;

    public static Comment createCommentFromCommand(CreateCommentCommand command) {
        return new Comment(
                UUID.randomUUID(),
                command.userId(),
                command.postId(),
                command.body(),
                Instant.now(ClockConfig.utcClock())
        );
    }
}
