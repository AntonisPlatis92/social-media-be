package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.domain.commands.LoginUserCommand;

public interface LoginUserUseCase {
    String loginUser(LoginUserCommand command);
}
