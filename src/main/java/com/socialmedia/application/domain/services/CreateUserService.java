package com.socialmedia.application.domain.services;

import com.socialmedia.application.domain.entities.User;
import com.socialmedia.application.domain.utils.clock.ClockConfig;
import com.socialmedia.application.domain.utils.database.DatabaseUtils;
import com.socialmedia.application.domain.utils.encoders.PasswordEncoder;
import com.socialmedia.application.domain.utils.exceptions.PasswordMinimumCharactersException;
import com.socialmedia.application.domain.utils.exceptions.UserAlreadyCreatedException;
import com.socialmedia.application.ports.out.CreateUserPort;
import com.socialmedia.application.ports.out.LoadUserPort;

import java.time.Instant;

public class CreateUserService {
    private final CreateUserPort createUserPort;
    private final LoadUserPort loadUserPort;
    private static final Long MINIMUM_PASSWORD_CHARACTERS = 8L;

    public CreateUserService(CreateUserPort createUserPort, LoadUserPort loadUserPort) {
        this.createUserPort = createUserPort;
        this.loadUserPort = loadUserPort;
    }

    public boolean createUser(String email, String password, long roleId) {
        if (password.length() < MINIMUM_PASSWORD_CHARACTERS) {
            throw new PasswordMinimumCharactersException(String.format("Password must be at least %s characters long.", MINIMUM_PASSWORD_CHARACTERS));
        }

        if (checkIfUserAlreadyCreated(email)) {
            throw new UserAlreadyCreatedException("User is already created.");
        }

        var hashedPassword = PasswordEncoder.encode(password);

        DatabaseUtils.doInTransaction((conn) -> {
            createUserPort.createUser(email,
                    hashedPassword,
                    false,
                    roleId,
                    Instant.now(ClockConfig.utcClock()));
        });

        return true;
    }

    private boolean checkIfUserAlreadyCreated(String email) {
        User userInDb = DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUser(email));
        return userInDb != null;
    }
}
