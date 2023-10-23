package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.accounts.application.port.out.CreateFollowPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.in.CreateFollowUseCase;
import com.socialmedia.accounts.domain.commands.CreateFollowCommand;
import com.socialmedia.posts.application.port.in.FollowingPostsMemoryUseCase;


public class CreateFollowService implements CreateFollowUseCase {
    private LoadUserPort loadUserPort;
    private CreateFollowPort createFollowPort;
    private FollowingPostsMemoryUseCase followingPostsCacheUseCase;

    public CreateFollowService(
            LoadUserPort loadUserPort,
            CreateFollowPort createFollowPort,
            FollowingPostsMemoryUseCase followingPostsCacheUseCase) {
        this.loadUserPort = loadUserPort;
        this.createFollowPort = createFollowPort;
        this.followingPostsCacheUseCase = followingPostsCacheUseCase;
    }

    @Override
    public void createNewFollow(CreateFollowCommand command) {
        User followerUser = loadUserPort.loadUserById(command.followerUserId()).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", command.followerUserId())));
        User followingUser = loadUserPort.loadUserByEmail(command.followingUserEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", command.followingUserEmail())));

        followerUser.follow(followingUser, createFollowPort);

        followingPostsCacheUseCase.addUserInFollowingPostsMemoryIfNeeded(followerUser);
    }
}
