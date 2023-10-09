package com.socialmedia.services;


import com.socialmedia.entities.User;
import com.socialmedia.repositories.UserRepository;
import com.socialmedia.utils.exceptions.PasswordMinimumCharactersException;
import com.socialmedia.utils.exceptions.UserAlreadyCreatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class CreateUserServiceTest {
    private CreateUserService sut;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new CreateUserService(userRepository);
    }

    @Test
    public void createUser_whenPasswordAtLeastEightCharsAndUserNew_saveSuccessfully() {
        //  Given
        var email = "test@test.com";
        var password = "12345678";
        var roleId = 1L;
        when(userRepository.findById(email)).thenReturn(null);
        var user = new User(
                email,
                BCrypt.hashpw(password, BCrypt.gensalt()),
                false,
                roleId,
                Instant.now()
        );
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        var userCreated = sut.createUser(email, password, roleId);

        // Then
        assertTrue(userCreated);
    }

    @Test
    public void createUser_whenPasswordAtLeastEightCharsAndUserExists_shouldThrowUserAlreadyCreated() {
        //  Given
        var email = "test@test.com";
        var password = "12345678";
        var roleId = 1L;
        var user = new User(
                email,
                BCrypt.hashpw(password, BCrypt.gensalt()),
                false,
                roleId,
                Instant.now()
        );
        when(userRepository.findById(email)).thenReturn(user);

        // When
        assertThrows(UserAlreadyCreatedException.class, () -> sut.createUser(email, password, roleId));

    }

    @Test
    public void createUser_whenPasswordLessThanEightCharsAndUserExists_shouldThrowPasswordMinimumCharactersException() {
        //  Given
        var email = "test@test.com";
        var password = "1234567";
        var roleId = 1L;

        // When
        assertThrows(PasswordMinimumCharactersException.class, () -> sut.createUser(email, password, roleId));

    }
}
