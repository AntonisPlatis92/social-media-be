package com.socialmedia.posts.domain;

import com.socialmedia.config.ClockConfig;
import com.socialmedia.posts.domain.commands.CreatePostCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Post {
    private UUID id;
    private String userEmail;
    private String body;
    private Instant creationTime;
    private List<Comment> comments;

    public static Post createPostFromCommand(CreatePostCommand command) {
        return new Post(
                UUID.randomUUID(),
                command.userEmail(),
                command.body(),
                Instant.now(ClockConfig.utcClock()),
                Collections.emptyList()
        );
    }
}
