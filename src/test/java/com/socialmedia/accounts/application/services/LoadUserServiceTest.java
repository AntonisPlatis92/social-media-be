package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.config.ClockConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LoadUserServiceTest {
    private LoadUserService sut;

    @Mock
    private LoadUserPort loadUserPort;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new LoadUserService(loadUserPort);
    }

    @Test
    void userReturned_whenPortReturnsUser_shouldReturnOptionalUser() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User(
                userId,
                "test",
                "test",
                false,
                1L,
                Instant.now(ClockConfig.utcClock())
        );
        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.of(user));

        // When
        Optional<User> userReturned = sut.loadUser(userId);

        // Then
        assertEquals(userReturned, Optional.of(user));
    }

    @Test
    void userReturned_whenPortReturnsEmpty_shouldReturnOptionalEmpty() {
        // Given
        UUID userId = UUID.randomUUID();
        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> userReturned = sut.loadUser(userId);

        // Then
        assertEquals(userReturned, Optional.empty());
    }
}
