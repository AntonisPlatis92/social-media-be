package com.socialmedia.services;


import com.socialmedia.application.domain.entities.User;
import com.socialmedia.application.domain.services.CreateUserService;
import com.socialmedia.application.domain.utils.clock.ClockConfig;
import com.socialmedia.application.domain.utils.encoders.PasswordEncoder;
import com.socialmedia.application.domain.utils.exceptions.PasswordMinimumCharactersException;
import com.socialmedia.application.domain.utils.exceptions.UserAlreadyCreatedException;
import com.socialmedia.application.ports.out.CreateUserPort;
import com.socialmedia.application.ports.out.LoadUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


class CreateUserServiceTest {
    private CreateUserService sut;

    @Mock
    private LoadUserPort loadUserPort;
    @Mock
    private CreateUserPort createUserPort;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new CreateUserService(createUserPort, loadUserPort);
    }

    @Test
    public void createUser_whenPasswordAtLeastEightCharsAndUserNew_saveSuccessfully() {
        //  Given
        var email = "test@test.com";
        var password = "12345678";
        var roleId = 1L;
        when(loadUserPort.loadUser(email)).thenReturn(null);
        var user = new User(
                email,
                PasswordEncoder.encode(password),
                false,
                roleId,
                Instant.now(ClockConfig.utcClock())
        );
        doNothing().when(createUserPort).createUser(eq(email), eq(PasswordEncoder.encode(password)), eq(false), eq(roleId), any(Instant.class));

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
                Instant.now(ClockConfig.utcClock())
        );
        when(loadUserPort.loadUser(email)).thenReturn(user);

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
