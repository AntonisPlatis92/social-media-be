package com.socialmedia.services;

import com.socialmedia.application.domain.entities.User;
import com.socialmedia.application.domain.services.LoginUserService;
import com.socialmedia.application.domain.utils.clock.ClockConfig;
import com.socialmedia.application.domain.utils.exceptions.UserNotFoundException;
import com.socialmedia.application.ports.out.LoadUserPort;
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
        when(loadUserPort.loadUser(email)).thenReturn(userInDb);

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
        when(loadUserPort.loadUser(email)).thenReturn(userInDb);

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
        when(loadUserPort.loadUser(email)).thenReturn(null);

        // When
        assertThrows(UserNotFoundException.class, () -> sut.loginUser(email, password));
    }
}
