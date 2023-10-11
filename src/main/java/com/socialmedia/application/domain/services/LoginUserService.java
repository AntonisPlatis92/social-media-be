package com.socialmedia.application.domain.services;

import com.socialmedia.application.domain.entities.User;
import com.socialmedia.application.domain.utils.authentication.JwtUtils;
import com.socialmedia.application.domain.utils.database.DatabaseUtils;
import com.socialmedia.application.domain.utils.encoders.PasswordEncoder;
import com.socialmedia.application.domain.utils.exceptions.UserNotFoundException;
import com.socialmedia.application.port.in.LoginUserCommand;
import com.socialmedia.application.port.in.LoginUserUseCase;
import com.socialmedia.application.port.out.LoadUserPort;

public class LoginUserService implements LoginUserUseCase {

    private final LoadUserPort loadUserPort;

    public LoginUserService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    public String loginUser(LoginUserCommand command) {

        User userInDb = DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUser(command.email()));

        if (userInDb == null) {
            throw new UserNotFoundException("User doesn't exist.");
        }

        boolean passwordMatch = PasswordEncoder.checkIfMatch(command.password(), userInDb.getHashedPassword());

        return (passwordMatch) ? JwtUtils.createToken(command.email()) : null;
    }
}
