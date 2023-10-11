package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.accounts.application.exceptions.UserAlreadyVerifiedException;
import com.socialmedia.accounts.application.exceptions.UserNotFoundException;
import com.socialmedia.accounts.application.port.in.VerifyUserCommand;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.out.VerifyUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VerifyUserServiceTest {
    private VerifyUserService sut;

    @Mock
    private LoadUserPort loadUserPort;
    @Mock
    private VerifyUserPort verifyUserPort;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new VerifyUserService(loadUserPort, verifyUserPort);
    }

    @Test
    public void verifyUser_whenUserExistsAndUnverified_shouldVerify() {
        //  Given
        String email = "test@test.com";
        VerifyUserCommand command = new VerifyUserCommand(email);

        User userInDb = new User(
                email,
                "hashedPassword",
                false,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(loadUserPort.loadUser(email)).thenReturn(userInDb);

        // When
        sut.verifyUser(command);

        // Then
        verify(verifyUserPort).verifyUser(email);
    }

    @Test
    public void verifyUser_whenUserExistsAndUnverified_shouldThrowUserAlreadyVerified() {
        //  Given
        String email = "test@test.com";
        VerifyUserCommand command = new VerifyUserCommand(email);

        User userInDb = new User(
                email,
                "hashedPassword",
                true,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(loadUserPort.loadUser(email)).thenReturn(userInDb);

        // When
        assertThrows(UserAlreadyVerifiedException.class, () -> sut.verifyUser(command));
    }

    @Test
    public void verifyUser_whenUserDoesNotExist_shouldThrowEntityNotFound() {
        //  Given
        String email = "test@test.com";
        VerifyUserCommand command = new VerifyUserCommand(email);

        when(loadUserPort.loadUser(email)).thenReturn(null);

        // When
        assertThrows(UserNotFoundException.class, () -> sut.verifyUser(command));
    }
}
