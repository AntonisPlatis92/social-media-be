package com.socialmedia.posts.application.services;

import com.socialmedia.accounts.application.port.in.LoadRoleUseCase;
import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.RoleNotFoundException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.posts.domain.exceptions.PostCharsLimitException;
import com.socialmedia.posts.application.port.in.CreatePostUseCase;
import com.socialmedia.posts.application.port.out.CreatePostPort;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.posts.domain.commands.CreatePostCommand;

public class CreatePostService implements CreatePostUseCase {
    private final LoadUserUseCase loadUserUseCase;
    private final LoadRoleUseCase loadRoleUseCase;
    private final CreatePostPort createPostPort;

    public CreatePostService(LoadUserUseCase loadUserUseCase, LoadRoleUseCase loadRoleUseCase, CreatePostPort createPostPort) {
        this.loadUserUseCase = loadUserUseCase;
        this.loadRoleUseCase = loadRoleUseCase;
        this.createPostPort = createPostPort;
    }
    @Override
    public void createPost(CreatePostCommand command) {
        User user = loadUserUseCase.loadUserByEmail(command.userEmail()).orElseThrow(() -> new UserNotFoundException("User doesn't exist."));

        Long roleId = user.getRoleId();
        Role role = loadRoleUseCase.loadRole(roleId).orElseThrow(() -> new RoleNotFoundException("Role doesn't exist."));

        boolean shouldCheckPostCharsLimit = role.isHasPostCharsLimit();
        if (shouldCheckPostCharsLimit) {
            checkPostCharsLimit(command.body(), role);
        }

        createPostPort.createNewPost(Post.createPostFromCommand(command));
    }

    private void checkPostCharsLimit(String postBody, Role role) {
        if (postBody.length() > role.getPostCharsLimit()) {
            throw new PostCharsLimitException(String.format("Posts have a limit of %d characters for current role.", role.getPostCharsLimit()));
        }
    }
}
