package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.domain.commands.CreateFollowCommand;

public interface CreateFollowUseCase {
    void createNewFollow(CreateFollowCommand command);
}
