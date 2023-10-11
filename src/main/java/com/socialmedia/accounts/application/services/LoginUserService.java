package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.utils.authentication.JwtUtils;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.utils.encoders.PasswordEncoder;
import com.socialmedia.accounts.application.exceptions.UserNotFoundException;
import com.socialmedia.accounts.application.port.in.LoginUserCommand;
import com.socialmedia.accounts.application.port.in.LoginUserUseCase;
import com.socialmedia.accounts.application.port.out.LoadUserPort;

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
