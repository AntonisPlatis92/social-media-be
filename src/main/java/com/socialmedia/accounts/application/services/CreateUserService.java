package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.utils.encoders.PasswordEncoder;
import com.socialmedia.accounts.application.exceptions.UserAlreadyCreatedException;
import com.socialmedia.accounts.application.port.in.CreateUserCommand;
import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;

import java.time.Instant;

public class CreateUserService implements CreateUserUseCase {
    private final CreateUserPort createUserPort;
    private final LoadUserPort loadUserPort;


    public CreateUserService(CreateUserPort createUserPort, LoadUserPort loadUserPort) {
        this.createUserPort = createUserPort;
        this.loadUserPort = loadUserPort;
    }

    public boolean createUser(CreateUserCommand command) {

        if (checkIfUserAlreadyCreated(command.email())) {
            throw new UserAlreadyCreatedException("User is already created.");
        }

        String hashedPassword = PasswordEncoder.encode(command.password());

        DatabaseUtils.doInTransaction((conn) -> {
            createUserPort.createUser(command.email(),
                    hashedPassword,
                    false,
                    command.roleId(),
                    Instant.now(ClockConfig.utcClock()));
        });

        return true;
    }

    private boolean checkIfUserAlreadyCreated(String email) {
        User userInDb = DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUser(email));
        return userInDb != null;
    }
}
