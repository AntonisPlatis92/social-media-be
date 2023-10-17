package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.accounts.application.port.out.CreateFollowPort;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.accounts.domain.exceptions.FollowAlreadyExistsException;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.in.CreateFollowUseCase;
import com.socialmedia.accounts.application.port.out.LoadFollowPort;
import com.socialmedia.accounts.domain.commands.CreateFollowCommand;

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

        createFollowPort.createFollow(Follow.createFollowFromCommand(command));
    }

    private void checkIfFollowAlreadyExists(CreateFollowCommand command) {
        loadFollowPort.loadFollowByPk(command.followerId(), command.followingId()).orElseThrow(() -> new FollowAlreadyExistsException("Follow already exists."));
    }

    private void checkIfUserDoesNotExist(UUID userId) {
        loadUserPort.loadUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", userId)));
    }
}
