package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.domain.commands.RemoveFollowCommand;

public interface RemoveFollowUseCase {
    void removeFollow(RemoveFollowCommand command);
}
