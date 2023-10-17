package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.domain.commands.CreateFollowCommand;

public interface CreateFollowUseCase {
    public void createNewFollow(CreateFollowCommand command);
}
