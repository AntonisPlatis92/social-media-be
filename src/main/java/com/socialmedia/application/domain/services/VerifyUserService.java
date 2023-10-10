package com.socialmedia.application.domain.services;

import com.socialmedia.application.domain.entities.User;
import com.socialmedia.application.domain.utils.database.DatabaseUtils;
import com.socialmedia.application.domain.utils.exceptions.UserAlreadyVerifiedException;
import com.socialmedia.application.domain.utils.exceptions.UserNotFoundException;
import com.socialmedia.application.ports.out.LoadUserPort;
import com.socialmedia.application.ports.out.VerifyUserPort;

public class VerifyUserService {
    private final LoadUserPort loadUserPort;
    private final VerifyUserPort verifyUserPort;

    public VerifyUserService(LoadUserPort loadUserPort, VerifyUserPort verifyUserPort) {
        this.loadUserPort = loadUserPort;
        this.verifyUserPort = verifyUserPort;
    }

    public void verifyUser(String email) {

        User userInDb = DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUser(email));

        if (userInDb == null) {
            throw new UserNotFoundException("User doesn't exist.");
        }

        if (userInDb.isVerified()) {
            throw new UserAlreadyVerifiedException(String.format("User %s is already verified",email));
        }

        DatabaseUtils.doInTransaction((conn) -> verifyUserPort.verifyUser(email));
    }
}
