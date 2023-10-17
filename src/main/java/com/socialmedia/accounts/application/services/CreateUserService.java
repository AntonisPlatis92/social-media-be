package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.accounts.domain.exceptions.UserAlreadyCreatedException;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;

public class CreateUserService implements CreateUserUseCase {
    private final CreateUserPort createUserPort;
    private final LoadUserPort loadUserPort;


    public CreateUserService(CreateUserPort createUserPort, LoadUserPort loadUserPort) {
        this.createUserPort = createUserPort;
        this.loadUserPort = loadUserPort;
    }

    public void createUser(CreateUserCommand command) {
        checkIfUserAlreadyCreated(command.email());

        DatabaseUtils.doInTransaction((conn) ->
                createUserPort.createUser(User.createUserFromCommand(command))
        );
    }

    private void checkIfUserAlreadyCreated(String email) {
        loadUserPort.loadUserByEmail(email).ifPresent(s -> {throw new UserAlreadyCreatedException("User is already created.");});
    }
}
