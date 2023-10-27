package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.accounts.domain.exceptions.RoleNotFoundException;
import com.socialmedia.accounts.domain.exceptions.UserAlreadyCreatedException;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;

public class CreateUserService implements CreateUserUseCase {
    private final LoadUserPort loadUserPort;
    private final LoadRolePort loadRolePort;
    private final CreateUserPort createUserPort;


    public CreateUserService(LoadUserPort loadUserPort, LoadRolePort loadRolePort, CreateUserPort createUserPort) {
        this.loadUserPort = loadUserPort;
        this.loadRolePort = loadRolePort;
        this.createUserPort = createUserPort;
    }

    public void createUser(CreateUserCommand command) {
        checkIfUserAlreadyCreated(command.email());

        Role role = loadRolePort.loadRoleById(command.roleId()).orElseThrow(() -> new RoleNotFoundException("Role doesn't exist."));
        createUserPort.createUser(User.createUserFromCommandAndRole(command,role));
    }

    private void checkIfUserAlreadyCreated(String email) {
        loadUserPort.loadUserByEmail(email).ifPresent(s -> {throw new UserAlreadyCreatedException("User is already created.");});
    }
}
