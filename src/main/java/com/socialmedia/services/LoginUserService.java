package com.socialmedia.services;

import com.socialmedia.repositories.UserRepository;
import com.socialmedia.utils.authentication.JwtUtils;
import com.socialmedia.utils.encoders.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityNotFoundException;

public class LoginUserService {

    private final UserRepository userRepository;

    public LoginUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String loginUser(String email, String password) {

        var userInDb = userRepository.findById(email);

        if (userInDb == null) {
            throw new EntityNotFoundException("User doesn't exist.");
        }

        boolean passwordMatch = PasswordEncoder.checkIfMatch(password, userInDb.getHashedPassword());

        return (passwordMatch) ? JwtUtils.createToken(email) : null;
    }
}
