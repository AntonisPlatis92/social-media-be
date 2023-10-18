package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.accounts.application.port.out.CreateFollowPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.in.CreateFollowUseCase;
import com.socialmedia.accounts.domain.commands.CreateFollowCommand;


public class CreateFollowService implements CreateFollowUseCase {
    private LoadUserPort loadUserPort;
    private CreateFollowPort createFollowPort;

    public CreateFollowService(
            LoadUserPort loadUserPort,
            CreateFollowPort createFollowPort) {
        this.loadUserPort = loadUserPort;
        this.createFollowPort = createFollowPort;
    }

    @Override
    public void createNewFollow(CreateFollowCommand command) {
        User followerUser = loadUserPort.loadUserByEmail(command.followerUserEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", command.followerUserEmail())));
        User followingUser = loadUserPort.loadUserByEmail(command.followingUserEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", command.followingUserEmail())));

        followerUser.follow(followingUser, createFollowPort);
    }
}
