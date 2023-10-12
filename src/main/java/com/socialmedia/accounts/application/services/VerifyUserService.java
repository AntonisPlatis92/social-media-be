package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.domain.exceptions.UserAlreadyVerifiedException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.accounts.domain.commands.VerifyUserCommand;
import com.socialmedia.accounts.application.port.in.VerifyUserUseCase;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.out.VerifyUserPort;

import java.util.Optional;

public class VerifyUserService implements VerifyUserUseCase {
    private final LoadUserPort loadUserPort;
    private final VerifyUserPort verifyUserPort;

    public VerifyUserService(LoadUserPort loadUserPort, VerifyUserPort verifyUserPort) {
        this.loadUserPort = loadUserPort;
        this.verifyUserPort = verifyUserPort;
    }

    public void verifyUser(VerifyUserCommand command) {

        Optional<User> maybeUserInDb = DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUser(command.email()));

        if (maybeUserInDb.isEmpty()) {
            throw new UserNotFoundException("User doesn't exist.");
        }

        if (maybeUserInDb.get().isVerified()) {
            throw new UserAlreadyVerifiedException(String.format("User %s is already verified",command.email()));
        }

        DatabaseUtils.doInTransaction((conn) -> verifyUserPort.verifyUser(command.email()));
    }
}
