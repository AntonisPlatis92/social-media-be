package com.socialmedia.application.domain.services;

import com.socialmedia.application.domain.entities.User;
import com.socialmedia.application.domain.utils.database.DatabaseUtils;
import com.socialmedia.application.domain.utils.exceptions.UserAlreadyVerifiedException;
import com.socialmedia.application.domain.utils.exceptions.UserNotFoundException;
import com.socialmedia.application.port.in.VerifyUserCommand;
import com.socialmedia.application.port.in.VerifyUserUseCase;
import com.socialmedia.application.port.out.LoadUserPort;
import com.socialmedia.application.port.out.VerifyUserPort;

public class VerifyUserService implements VerifyUserUseCase {
    private final LoadUserPort loadUserPort;
    private final VerifyUserPort verifyUserPort;

    public VerifyUserService(LoadUserPort loadUserPort, VerifyUserPort verifyUserPort) {
        this.loadUserPort = loadUserPort;
        this.verifyUserPort = verifyUserPort;
    }

    public void verifyUser(VerifyUserCommand command) {

        User userInDb = DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUser(command.email()));

        if (userInDb == null) {
            throw new UserNotFoundException("User doesn't exist.");
        }

        if (userInDb.isVerified()) {
            throw new UserAlreadyVerifiedException(String.format("User %s is already verified",command.email()));
        }

        DatabaseUtils.doInTransaction((conn) -> verifyUserPort.verifyUser(command.email()));
    }
}
