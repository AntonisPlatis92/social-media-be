package com.socialmedia.services;

import com.socialmedia.entities.User;
import com.socialmedia.repositories.UserRepository;
import com.socialmedia.utils.clock.ClockConfig;
import com.socialmedia.utils.exceptions.UserAlreadyVerifiedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VerifyUserServiceTest {
    private VerifyUserService sut;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new VerifyUserService(userRepository);
    }

    @Test
    public void verifyUser_whenUserExistsAndUnverified_shouldVerify() {
        //  Given
        var email = "test@test.com";

        var userInDb = new User(
                email,
                "hashedPassword",
                false,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(userRepository.findById(email)).thenReturn(userInDb);

        // When
        sut.verifyUser(email);

        // Then
        verify(userRepository).save(userCaptor.capture());
        assertTrue(userCaptor.getValue().isVerified());
    }

    @Test
    public void verifyUser_whenUserExistsAndUnverified_shouldThrowUserAlreadyVerified() {
        //  Given
        var email = "test@test.com";

        var userInDb = new User(
                email,
                "hashedPassword",
                true,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(userRepository.findById(email)).thenReturn(userInDb);

        // When
        assertThrows(UserAlreadyVerifiedException.class, () -> sut.verifyUser(email));
    }

    @Test
    public void verifyUser_whenUserDoesNotExist_shouldThrowEntityNotFound() {
        //  Given
        var email = "test@test.com";

        when(userRepository.findById(email)).thenReturn(null);

        // When
        assertThrows(EntityNotFoundException.class, () -> sut.verifyUser(email));
    }
}
