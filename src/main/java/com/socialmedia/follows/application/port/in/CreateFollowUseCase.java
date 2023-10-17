package com.socialmedia.follows.application.port.in;

import com.socialmedia.follows.domain.commands.CreateFollowCommand;

public interface CreateFollowUseCase {
    public void createNewFollow(CreateFollowCommand command);
}
