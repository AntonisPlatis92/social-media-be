package com.socialmedia.services;

import com.socialmedia.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityNotFoundException;

public class LoginUserService {

    private final UserRepository userRepository;

    public LoginUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean loginUser(String email, String password) {

        var userInDb = userRepository.findById(email);

        if (userInDb == null) {
            throw new EntityNotFoundException("User doesn't exist.");
        }

        return BCrypt.checkpw(password, userInDb.getHashedPassword());
    }
}
