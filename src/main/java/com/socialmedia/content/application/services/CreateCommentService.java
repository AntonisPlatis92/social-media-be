package com.socialmedia.content.application.services;

import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.RoleNotFoundException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.content.domain.exceptions.CommentsLimitException;
import com.socialmedia.content.domain.exceptions.PostNotFoundException;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.content.application.port.in.CreateCommentUseCase;
import com.socialmedia.content.application.port.out.CreateCommentPort;
import com.socialmedia.content.application.port.out.LoadCommentPort;
import com.socialmedia.content.application.port.out.LoadPostPort;
import com.socialmedia.content.domain.Comment;
import com.socialmedia.content.domain.Post;
import com.socialmedia.content.domain.commands.CreateCommentCommand;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class CreateCommentService implements CreateCommentUseCase {
    private final LoadUserPort loadUserPort;
    private final LoadRolePort loadRolePort;
    private final LoadPostPort loadPostPort;
    private final LoadCommentPort loadCommentPort;
    private final CreateCommentPort createCommentPort;

    public CreateCommentService(
            LoadUserPort loadUserPort,
            LoadRolePort loadRolePort,
            LoadPostPort loadPostPort,
            LoadCommentPort loadCommentPort,
            CreateCommentPort createCommentPort) {
        this.loadUserPort = loadUserPort;
        this.loadRolePort = loadRolePort;
        this.loadPostPort = loadPostPort;
        this.loadCommentPort = loadCommentPort;
        this.createCommentPort = createCommentPort;
    }
    @Override
    public void createComment(CreateCommentCommand command) {
        Optional<User> maybeUser = loadUserPort.loadUserById(command.userId());
        if (maybeUser.isEmpty()) {throw new UserNotFoundException("User doesn't exist.");}

        Long roleId = maybeUser.get().getRoleId();
        Optional<Role> maybeRole = loadRolePort.loadRoleById(roleId);
        if (maybeRole.isEmpty()) {throw new RoleNotFoundException("Role doesn't exist.");}

        Optional<Post> maybePost = loadPostPort.loadPostById(command.postId());
        if (maybePost.isEmpty()) {throw new PostNotFoundException("Post doesn't exist.");
        }

        boolean shouldCheckCommentsLimit = maybeRole.get().isHasCommentsLimit();
        if (shouldCheckCommentsLimit) {
            checkCommentsLimit(command.userId(), command.postId(), maybeRole.get());
        }

        Comment newComment = new Comment(
                UUID.randomUUID(),
                command.userId(),
                command.postId(),
                command.body(),
                Instant.now(ClockConfig.utcClock())
        );
        createCommentPort.createNewComment(newComment);
    }

    private void checkCommentsLimit(UUID userId, UUID postId, Role role) {
        int userCommentsInPost = loadCommentPort.loadCommentByUserIdAndPostId(
                userId,
                postId
        ).size();
        if (userCommentsInPost >= role.getCommentsLimit()) {
            throw new CommentsLimitException(String.format("Current role is restricted to %d comments per post.", role.getCommentsLimit()));
        }
    }
}
