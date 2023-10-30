package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.in.VerifyUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.commands.VerifyUserCommand;
import com.socialmedia.accounts.domain.exceptions.UserAlreadyVerifiedException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.out.VerifyUserPort;
import com.socialmedia.utils.database.JpaDatabaseUtils;

import java.util.Optional;

public class VerifyUserService implements VerifyUserUseCase {
    private final LoadUserPort loadUserPort;
    private final VerifyUserPort verifyUserPort;

    public VerifyUserService(LoadUserPort loadUserPort, VerifyUserPort verifyUserPort) {
        this.loadUserPort = loadUserPort;
        this.verifyUserPort = verifyUserPort;
    }

    public void verifyUser(VerifyUserCommand command) {

        JpaDatabaseUtils.doInTransaction(entityManager -> {
            Optional<User> maybeUserInDb = loadUserPort.loadUserByEmail(command.email());

            if (maybeUserInDb.isEmpty()) {
                throw new UserNotFoundException("User doesn't exist.");
            }

            if (maybeUserInDb.get().isVerified()) {
                throw new UserAlreadyVerifiedException(String.format("User %s is already verified",command.email()));
            }

            verifyUserPort.verifyUser(command.email());
        });
    }
}
