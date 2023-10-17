package com.socialmedia.content.domain;

import com.socialmedia.config.ClockConfig;
import com.socialmedia.content.domain.commands.CreateCommentCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Comment {
    private UUID id;
    private String userEmail;
    private UUID postId;
    private String body;
    private Instant creationTime;

    public static Comment createCommentFromCommand(CreateCommentCommand command) {
        return new Comment(
                UUID.randomUUID(),
                command.userEmail(),
                command.postId(),
                command.body(),
                Instant.now(ClockConfig.utcClock())
        );
    }
}
