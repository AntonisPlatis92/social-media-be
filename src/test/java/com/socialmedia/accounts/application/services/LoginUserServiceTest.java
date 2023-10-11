package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.accounts.application.exceptions.UserNotFoundException;
import com.socialmedia.accounts.application.port.in.LoginUserCommand;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class LoginUserServiceTest {
    private LoginUserService sut;

    @Mock
    private LoadUserPort loadUserPort;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new LoginUserService(loadUserPort);
    }

    @Test
    public void loginUser_whenUserExistsAndCredentialsCorrect_shouldLogin() {
        //  Given
        String email = "test@test.com";
        String password = "rawPassword";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        LoginUserCommand command = new LoginUserCommand(email, password);

        User userInDb = new User(
                email,
                hashedPassword,
                true,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(loadUserPort.loadUser(email)).thenReturn(userInDb);

        // When
        String userToken = sut.loginUser(command);

        // Then
        assertNotNull(userToken);
    }

    @Test
    public void loginUser_whenUserExistsAndCredentialsWrong_shouldLogin() {
        //  Given
        String email = "test@test.com";
        String password = "rawPassword";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        LoginUserCommand command = new LoginUserCommand(email, password);

        User userInDb = new User(
                email,
                hashedPassword+"1",
                true,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(loadUserPort.loadUser(email)).thenReturn(userInDb);

        // When
        String userToken = sut.loginUser(command);

        // Then
        assertNull(userToken);
    }

    @Test
    public void loginUser_whenUserDoesNotExist_shouldThrowEntityNotFound() {
        //  Given
        String email = "test@test.com";
        String password = "rawPassword";
        LoginUserCommand command = new LoginUserCommand(email, password);
        when(loadUserPort.loadUser(email)).thenReturn(null);

        // When
        assertThrows(UserNotFoundException.class, () -> sut.loginUser(command));
    }
}