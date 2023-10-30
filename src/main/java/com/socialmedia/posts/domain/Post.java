package com.socialmedia.posts.domain;

import com.socialmedia.accounts.domain.Role;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.posts.application.port.out.CreateCommentPort;
import com.socialmedia.posts.domain.commands.CreateCommentCommand;
import com.socialmedia.posts.domain.commands.CreatePostCommand;
import com.socialmedia.posts.domain.exceptions.CommentsLimitException;
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
    private UUID userId;
    private String body;
    private Instant creationTime;
    private List<Comment> comments;

    public static Post createPostFromCommand(CreatePostCommand command) {
        return new Post(
                UUID.randomUUID(),
                command.userId(),
                command.body(),
                Instant.now(ClockConfig.utcClock()),
                Collections.emptyList()
        );
    }

    public void addComment(CreateCommentCommand command,
                           Role role,
                           CreateCommentPort createCommentPort) {

        if (role.isHasCommentsLimit()) {
            checkCommentsLimit(command.userId(), role);
        }

        createCommentPort.createNewComment(Comment.createCommentFromCommand(command));
    }

    private void checkCommentsLimit(UUID commentUserId, Role role) {
        long commentsOnPostByUser = comments.stream()
                .filter(comment -> commentUserId.equals(comment.getUserId()))
                .count();
        if (commentsOnPostByUser >= role.getCommentsLimit()) {
            throw new CommentsLimitException(String.format("Current role is restricted to %d comments per post.", role.getCommentsLimit()));
        }
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
