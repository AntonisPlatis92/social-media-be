package com.socialmedia.services;

import com.socialmedia.repositories.UserRepository;
import com.socialmedia.utils.exceptions.UserAlreadyVerifiedException;

import javax.persistence.EntityNotFoundException;

public class VerifyUserService {
    private final UserRepository userRepository;

    public VerifyUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void verifyUser(String email) {

        var userInDb = userRepository.findById(email);

        if (userInDb == null) {
            throw new EntityNotFoundException("User doesn't exist.");
        }

        if (userInDb.isVerified()) {
            throw new UserAlreadyVerifiedException(String.format("User %s is already verified",email));
        }

        userInDb.verifyUser();

        userRepository.save(userInDb);
    }
}
