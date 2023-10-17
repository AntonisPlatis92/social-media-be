package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.in.LoginUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.commands.LoginUserCommand;
import com.socialmedia.accounts.domain.exceptions.LoginFailedException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.utils.authentication.JwtUtils;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.utils.encoders.PasswordEncoder;
import com.socialmedia.accounts.application.port.out.LoadUserPort;

import java.util.Optional;

public class LoginUserService implements LoginUserUseCase {

    private final LoadUserPort loadUserPort;

    public LoginUserService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    public String loginUser(LoginUserCommand command) {

        Optional<User> maybeUserInDb = DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUserByEmail(command.email()));

        if (maybeUserInDb.isEmpty()) {
            throw new UserNotFoundException("User doesn't exist.");
        }

        boolean passwordMatch = PasswordEncoder.checkIfMatch(command.password(), maybeUserInDb.get().getHashedPassword());

        if (!passwordMatch) {throw new LoginFailedException("Wrong credentials. User login failed.");}

        return JwtUtils.createToken(maybeUserInDb.get().getUserId());
    }
}
