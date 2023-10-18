package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.in.RemoveFollowUseCase;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.out.RemoveFollowPort;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.commands.RemoveFollowCommand;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;

public class RemoveFollowService implements RemoveFollowUseCase {
    private LoadUserPort loadUserPort;
    private RemoveFollowPort removeFollowPort;

    public RemoveFollowService(
            LoadUserPort loadUserPort,
            RemoveFollowPort removeFollowPort) {
        this.loadUserPort = loadUserPort;
        this.removeFollowPort = removeFollowPort;
    }
    @Override
    public void removeFollow(RemoveFollowCommand command) {
        User followerUser = loadUserPort.loadUserById(command.followerId()).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", command.followerId())));
        User followingUser = loadUserPort.loadUserById(command.followingId()).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", command.followingId())));

        followerUser.unfollow(followingUser, removeFollowPort);
    }
}
