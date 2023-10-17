package com.socialmedia.follows.application.services;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.follows.application.port.out.CreateFollowPort;
import com.socialmedia.follows.domain.Follow;
import com.socialmedia.follows.domain.exceptions.FollowAlreadyExistsException;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.follows.application.port.in.CreateFollowUseCase;
import com.socialmedia.follows.application.port.out.LoadFollowPort;
import com.socialmedia.follows.domain.commands.CreateFollowCommand;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class CreateFollowService implements CreateFollowUseCase {
    private LoadFollowPort loadFollowPort;
    private LoadUserPort loadUserPort;
    private CreateFollowPort createFollowPort;

    public CreateFollowService(
            LoadFollowPort loadFollowPort,
            LoadUserPort loadUserPort,
            CreateFollowPort createFollowPort) {
        this.loadFollowPort = loadFollowPort;
        this.loadUserPort = loadUserPort;
        this.createFollowPort = createFollowPort;
    }

    @Override
    public void createNewFollow(CreateFollowCommand command) {
        if (checkIfFollowAlreadyExists(command)) {
            throw new FollowAlreadyExistsException("Follow already exists.");
        }
        if (checkIfUserDoesNotExist(command.followerId())) {
            throw new UserNotFoundException(String.format("User %s not found.", command.followerId()));
        }
        if (checkIfUserDoesNotExist(command.followingId())) {
            throw new UserNotFoundException(String.format("User %s not found.", command.followingId()));
        }

        Follow newFollow = new Follow(
                command.followerId(),
                command.followingId(),
                Instant.now(ClockConfig.utcClock())
        );
        createFollowPort.createFollow(newFollow);
    }

    private boolean checkIfFollowAlreadyExists(CreateFollowCommand command) {
        Optional<Follow> maybeFollow = loadFollowPort.loadFollowByPk(command.followerId(), command.followingId());
        return maybeFollow.isPresent();
    }

    private boolean checkIfUserDoesNotExist(UUID userId) {
        Optional<User> maybeUser = loadUserPort.loadUserById(userId);
        return maybeUser.isEmpty();
    }
}
