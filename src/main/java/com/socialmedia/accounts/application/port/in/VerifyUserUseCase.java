package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.domain.commands.VerifyUserCommand;

public interface VerifyUserUseCase {
    void verifyUser(VerifyUserCommand command);
}
