package unit.com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.services.LoginUserService;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.LoginFailedException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.accounts.domain.commands.LoginUserCommand;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import unit.com.socialmedia.accounts.domain.UserBuilder;

import java.util.Optional;

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

        User userInDb = UserBuilder.aRandomUserBuilder()
                .withEmail(email)
                .withHashedPassword(hashedPassword)
                .build();

        when(loadUserPort.loadUserByEmail(email)).thenReturn(Optional.of(userInDb));

        // When
        String userToken = sut.loginUser(command);

        // Then
        assertNotNull(userToken);
    }

    @Test
    public void loginUser_whenUserExistsAndCredentialsWrong_shouldThrowLoginFailedException() {
        //  Given
        String email = "test@test.com";
        String password = "rawPassword";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        LoginUserCommand command = new LoginUserCommand(email, password);

        User userInDb = UserBuilder.aRandomUserBuilder()
                .withEmail(email)
                .withHashedPassword(hashedPassword+"1")
                .build();
        when(loadUserPort.loadUserByEmail(email)).thenReturn(Optional.of(userInDb));

        // Then
        assertThrows(LoginFailedException.class, () -> sut.loginUser(command));
    }

    @Test
    public void loginUser_whenUserDoesNotExist_shouldThrowEntityNotFound() {
        //  Given
        String email = "test@test.com";
        String password = "rawPassword";
        LoginUserCommand command = new LoginUserCommand(email, password);
        when(loadUserPort.loadUserByEmail(email)).thenReturn(Optional.empty());

        // When
        assertThrows(UserNotFoundException.class, () -> sut.loginUser(command));
    }
}
