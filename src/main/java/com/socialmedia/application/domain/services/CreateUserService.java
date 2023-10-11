package com.socialmedia.application.domain.services;

import com.socialmedia.application.domain.entities.User;
import com.socialmedia.application.domain.utils.clock.ClockConfig;
import com.socialmedia.application.domain.utils.database.DatabaseUtils;
import com.socialmedia.application.domain.utils.encoders.PasswordEncoder;
import com.socialmedia.application.domain.utils.exceptions.PasswordMinimumCharactersException;
import com.socialmedia.application.domain.utils.exceptions.UserAlreadyCreatedException;
import com.socialmedia.application.port.in.CreateUserCommand;
import com.socialmedia.application.port.in.CreateUserUseCase;
import com.socialmedia.application.port.out.CreateUserPort;
import com.socialmedia.application.port.out.LoadUserPort;

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
