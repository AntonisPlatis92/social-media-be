package com.socialmedia.services;

import com.socialmedia.entities.User;
import com.socialmedia.repositories.UserRepository;
import com.socialmedia.utils.exceptions.PasswordMinimumCharactersException;
import com.socialmedia.utils.exceptions.UserAlreadyCreatedException;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;

public class CreateUserService {
    private final UserRepository userRepository;
    private static final Long MINIMUM_PASSWORD_CHARACTERS = 8L;

    public CreateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createUser(String email, String password, long roleId) {
        if (password.length() < MINIMUM_PASSWORD_CHARACTERS) {
            throw new PasswordMinimumCharactersException(String.format("Password must be at least %s characters long.", MINIMUM_PASSWORD_CHARACTERS));
        }

        if (checkIfUserAlreadyCreated(email)) {
            throw new UserAlreadyCreatedException("User is already created.");
        }

        var hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        var user = new User(
                email,
                hashedPassword,
                false,
                roleId,
                Instant.now()
        );
        var createdUser = userRepository.save(user);

        return createdUser != null;
    }

    private boolean checkIfUserAlreadyCreated(String email) {
        var userInDb = userRepository.findById(email);
        return userInDb != null;
    }
}
