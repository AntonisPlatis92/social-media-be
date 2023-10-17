package com.socialmedia.content.application.services;

import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.RoleNotFoundException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.content.domain.exceptions.PostCharsLimitException;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.content.application.port.in.CreatePostUseCase;
import com.socialmedia.content.application.port.out.CreatePostPort;
import com.socialmedia.content.domain.Post;
import com.socialmedia.content.domain.commands.CreatePostCommand;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class CreatePostService implements CreatePostUseCase {
    private final LoadUserPort loadUserPort;
    private final LoadRolePort loadRolePort;
    private final CreatePostPort createPostPort;

    public CreatePostService(LoadUserPort loadUserPort, LoadRolePort loadRolePort, CreatePostPort createPostPort) {
        this.loadUserPort = loadUserPort;
        this.loadRolePort = loadRolePort;
        this.createPostPort = createPostPort;
    }
    @Override
    public void createPost(CreatePostCommand command) {
        Optional<User> maybeUser = loadUserPort.loadUserById(command.userId());
        if (maybeUser.isEmpty()) {throw new UserNotFoundException("User doesn't exist.");}

        Long roleId = maybeUser.get().getRoleId();
        Optional<Role> maybeRole = loadRolePort.loadRoleById(roleId);
        if (maybeRole.isEmpty()) {throw new RoleNotFoundException("Role doesn't exist.");}

        boolean shouldCheckPostCharsLimit = maybeRole.get().isHasPostCharsLimit();
        if (shouldCheckPostCharsLimit) {
            checkPostCharsLimit(command.body(), maybeRole.get());
        }

        Post newPost = new Post(
                UUID.randomUUID(),
                command.userId(),
                command.body(),
                Instant.now(ClockConfig.utcClock())
        );
        createPostPort.createNewPost(newPost);
    }

    private void checkPostCharsLimit(String postBody, Role role) {
        if (postBody.length() > role.getPostCharsLimit()) {
            throw new PostCharsLimitException(String.format("Posts have a limit of %d characters for current role.", role.getPostCharsLimit()));
        }
    }
}
