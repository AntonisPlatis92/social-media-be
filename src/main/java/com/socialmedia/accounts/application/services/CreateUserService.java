package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.accounts.domain.exceptions.UserAlreadyCreatedException;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.utils.encoders.PasswordEncoder;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class CreateUserService implements CreateUserUseCase {
    private final CreateUserPort createUserPort;
    private final LoadUserPort loadUserPort;


    public CreateUserService(CreateUserPort createUserPort, LoadUserPort loadUserPort) {
        this.createUserPort = createUserPort;
        this.loadUserPort = loadUserPort;
    }

    public void createUser(CreateUserCommand command) {

        if (checkIfUserAlreadyCreated(command.email())) {
            throw new UserAlreadyCreatedException("User is already created.");
        }

        String hashedPassword = PasswordEncoder.encode(command.password());

        User userToBeCreated = new User(
                UUID.randomUUID(),
                command.email(),
                hashedPassword,
                false,
                command.roleId(),
                Instant.now(Clock.systemUTC())
        );

        DatabaseUtils.doInTransaction((conn) -> {
            createUserPort.createUser(userToBeCreated);
        });
    }

    private boolean checkIfUserAlreadyCreated(String email) {
        Optional<User> maybeUserInDb = DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUserByEmail(email));
        return maybeUserInDb.isPresent();
    }
}
