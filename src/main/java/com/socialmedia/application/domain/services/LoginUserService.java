package com.socialmedia.application.domain.services;

import com.socialmedia.application.domain.entities.User;
import com.socialmedia.application.domain.utils.authentication.JwtUtils;
import com.socialmedia.application.domain.utils.database.DatabaseUtils;
import com.socialmedia.application.domain.utils.encoders.PasswordEncoder;
import com.socialmedia.application.domain.utils.exceptions.UserNotFoundException;
import com.socialmedia.application.ports.out.LoadUserPort;

public class LoginUserService {

    private final LoadUserPort loadUserPort;

    public LoginUserService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    public String loginUser(String email, String password) {

        User userInDb = DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUser(email));

        if (userInDb == null) {
            throw new UserNotFoundException("User doesn't exist.");
        }

        boolean passwordMatch = PasswordEncoder.checkIfMatch(password, userInDb.getHashedPassword());

        return (passwordMatch) ? JwtUtils.createToken(email) : null;
    }
}
