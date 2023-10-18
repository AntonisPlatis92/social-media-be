package unit.com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.services.VerifyUserService;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserAlreadyVerifiedException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.accounts.domain.commands.VerifyUserCommand;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.out.VerifyUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import unit.com.socialmedia.accounts.domain.UserBuilder;

import java.util.Optional;

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

        User userInDb = UserBuilder.aRandomUserBuilder()
                .withEmail(email)
                .withVerified(false)
                .build();

        when(loadUserPort.loadUserByEmail(email)).thenReturn(Optional.of(userInDb));

        // When
        sut.verifyUser(command);

        // Then
        verify(verifyUserPort).verifyUser(email);
    }

    @Test
    public void verifyUser_whenUserExistsAndVerified_shouldThrowUserAlreadyVerified() {
        //  Given
        String email = "test@test.com";
        VerifyUserCommand command = new VerifyUserCommand(email);

        User userInDb = UserBuilder.aRandomUserBuilder()
                .withEmail(email)
                .withVerified(true)
                .build();
        when(loadUserPort.loadUserByEmail(email)).thenReturn(Optional.of(userInDb));

        // When
        assertThrows(UserAlreadyVerifiedException.class, () -> sut.verifyUser(command));
    }

    @Test
    public void verifyUser_whenUserDoesNotExist_shouldThrowEntityNotFound() {
        //  Given
        String email = "test@test.com";
        VerifyUserCommand command = new VerifyUserCommand(email);

        when(loadUserPort.loadUserByEmail(email)).thenReturn(Optional.empty());

        // When
        assertThrows(UserNotFoundException.class, () -> sut.verifyUser(command));
    }
}
