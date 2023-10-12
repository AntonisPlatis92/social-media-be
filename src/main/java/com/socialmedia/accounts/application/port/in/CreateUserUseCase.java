package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.domain.commands.CreateUserCommand;

public interface CreateUserUseCase {
    void createUser(CreateUserCommand command);
}
