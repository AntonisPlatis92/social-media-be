package com.socialmedia.content.application.services;

import com.socialmedia.accounts.application.port.in.LoadRoleUseCase;
import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.RoleNotFoundException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.content.domain.exceptions.CommentsLimitException;
import com.socialmedia.content.domain.exceptions.PostNotFoundException;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.content.application.port.in.CreateCommentUseCase;
import com.socialmedia.content.application.port.out.CreateCommentPort;
import com.socialmedia.content.application.port.out.LoadCommentPort;
import com.socialmedia.content.application.port.out.LoadPostPort;
import com.socialmedia.content.domain.Comment;
import com.socialmedia.content.domain.commands.CreateCommentCommand;

import java.time.Instant;
import java.util.UUID;

public class CreateCommentService implements CreateCommentUseCase {
    private final LoadUserUseCase loadUserUseCase;
    private final LoadRoleUseCase loadRoleUseCase;
    private final LoadPostPort loadPostPort;
    private final LoadCommentPort loadCommentPort;
    private final CreateCommentPort createCommentPort;

    public CreateCommentService(
            LoadUserUseCase loadUserUseCase,
            LoadRoleUseCase loadRoleUseCase,
            LoadPostPort loadPostPort,
            LoadCommentPort loadCommentPort,
            CreateCommentPort createCommentPort) {
        this.loadUserUseCase = loadUserUseCase;
        this.loadRoleUseCase = loadRoleUseCase;
        this.loadPostPort = loadPostPort;
        this.loadCommentPort = loadCommentPort;
        this.createCommentPort = createCommentPort;
    }
    @Override
    public void createComment(CreateCommentCommand command) {
        User user = loadUserUseCase.loadUserById(command.userId()).orElseThrow(() -> new UserNotFoundException("User doesn't exist."));

        Long roleId = user.getRoleId();
        Role role = loadRoleUseCase.loadRole(roleId).orElseThrow(() -> new RoleNotFoundException("Role doesn't exist."));

        loadPostPort.loadPostById(command.postId()).orElseThrow(() -> new PostNotFoundException("Post doesn't exist."));

        boolean shouldCheckCommentsLimit = role.isHasCommentsLimit();
        if (shouldCheckCommentsLimit) {
            checkCommentsLimit(command.userId(), command.postId(), role);
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
