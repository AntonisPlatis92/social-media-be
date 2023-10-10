package com.socialmedia.services;

import com.socialmedia.application.domain.entities.User;
import com.socialmedia.application.domain.services.VerifyUserService;
import com.socialmedia.application.domain.utils.clock.ClockConfig;
import com.socialmedia.application.domain.utils.exceptions.UserAlreadyVerifiedException;
import com.socialmedia.application.domain.utils.exceptions.UserNotFoundException;
import com.socialmedia.application.ports.out.LoadUserPort;
import com.socialmedia.application.ports.out.VerifyUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

        User userInDb = new User(
                email,
                "hashedPassword",
                false,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(loadUserPort.loadUser(email)).thenReturn(userInDb);

        // When
        sut.verifyUser(email);

        // Then
        verify(verifyUserPort).verifyUser(email);
    }

    @Test
    public void verifyUser_whenUserExistsAndUnverified_shouldThrowUserAlreadyVerified() {
        //  Given
        String email = "test@test.com";

        User userInDb = new User(
                email,
                "hashedPassword",
                true,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(loadUserPort.loadUser(email)).thenReturn(userInDb);

        // When
        assertThrows(UserAlreadyVerifiedException.class, () -> sut.verifyUser(email));
    }

    @Test
    public void verifyUser_whenUserDoesNotExist_shouldThrowEntityNotFound() {
        //  Given
        String email = "test@test.com";
        when(loadUserPort.loadUser(email)).thenReturn(null);

        // When
        assertThrows(UserNotFoundException.class, () -> sut.verifyUser(email));
    }
}
