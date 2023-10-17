package com.socialmedia.follows.application.services;

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
        checkIfFollowAlreadyExists(command);
        checkIfUserDoesNotExist(command.followerId());
        checkIfUserDoesNotExist(command.followingId());

        Follow newFollow = new Follow(
                command.followerId(),
                command.followingId(),
                Instant.now(ClockConfig.utcClock())
        );
        createFollowPort.createFollow(newFollow);
    }

    private void checkIfFollowAlreadyExists(CreateFollowCommand command) {
        loadFollowPort.loadFollowByPk(command.followerId(), command.followingId()).orElseThrow(() -> new FollowAlreadyExistsException("Follow already exists."));
    }

    private void checkIfUserDoesNotExist(UUID userId) {
        loadUserPort.loadUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", userId)));
    }
}
