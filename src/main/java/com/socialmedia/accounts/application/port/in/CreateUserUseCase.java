package com.socialmedia.accounts.application.port.in;

public interface CreateUserUseCase {
    boolean createUser(CreateUserCommand command);
}
