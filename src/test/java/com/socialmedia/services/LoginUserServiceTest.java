package com.socialmedia.services;

import com.socialmedia.entities.User;
import com.socialmedia.repositories.UserRepository;
import com.socialmedia.utils.clock.ClockConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class LoginUserServiceTest {
    private LoginUserService sut;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new LoginUserService(userRepository);
    }

    @Test
    public void loginUser_whenUserExistsAndCredentialsCorrect_shouldLogin() {
        //  Given
        var email = "test@test.com";
        var password = "rawPassword";
        var hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        var userInDb = new User(
                email,
                hashedPassword,
                true,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(userRepository.findById(email)).thenReturn(userInDb);

        // When
        String userToken = sut.loginUser(email, password);

        // Then
        assertNotNull(userToken);
    }

    @Test
    public void loginUser_whenUserExistsAndCredentialsWrong_shouldLogin() {
        //  Given
        var email = "test@test.com";
        var password = "rawPassword";
        var hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        var userInDb = new User(
                email,
                hashedPassword+"1",
                true,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(userRepository.findById(email)).thenReturn(userInDb);

        // When
        String userToken = sut.loginUser(email, password);

        // Then
        assertNull(userToken);
    }

    @Test
    public void loginUser_whenUserDoesNotExist_shouldThrowEntityNotFound() {
        //  Given
        var email = "test@test.com";
        var password = "rawPassword";
        when(userRepository.findById(email)).thenReturn(null);

        // When
        assertThrows(EntityNotFoundException.class, () -> sut.loginUser(email, password));
    }
}
