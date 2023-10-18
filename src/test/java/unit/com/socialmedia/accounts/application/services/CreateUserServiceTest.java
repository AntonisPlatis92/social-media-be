package unit.com.socialmedia.accounts.application.services;


import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.accounts.domain.exceptions.PasswordMinimumCharactersException;
import com.socialmedia.accounts.domain.exceptions.UserAlreadyCreatedException;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import unit.com.socialmedia.accounts.domain.RoleBuilder;


class CreateUserServiceTest {
    private CreateUserService sut;

    @Mock
    private LoadUserPort loadUserPort;
    @Mock
    private LoadRolePort loadRolePort;
    @Mock
    private CreateUserPort createUserPort;
    @Captor
    ArgumentCaptor<User> userCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new CreateUserService(loadUserPort, loadRolePort, createUserPort);
    }

    @Test
    public void createUser_whenPasswordAtLeastEightCharsAndUserNew_saveSuccessfully() {
        //  Given
        String email = "test@test.com";
        String password = "12345678";
        long roleId = 1L;
        when(loadUserPort.loadUserByEmail(email)).thenReturn(Optional.empty());
        when(loadRolePort.loadRoleById(roleId)).thenReturn(Optional.of(RoleBuilder.aFreeUserRoleBuilder().build()));
        doNothing().when(createUserPort).createUser(any(User.class));

        // When
        sut.createUser(new CreateUserCommand(email, password, roleId));

        // Then
        verify(createUserPort).createUser(userCaptor.capture());
        assertEquals(email, userCaptor.getValue().getEmail());
        assertFalse(userCaptor.getValue().isVerified());
        assertEquals(roleId, userCaptor.getValue().getRole().getId());
    }

    @Test
    public void createUser_whenPasswordAtLeastEightCharsAndUserExists_shouldThrowUserAlreadyCreated() {
        //  Given
        String email = "test@test.com";
        String password = "12345678";
        long roleId = 1L;
        User user = new User(
                UUID.randomUUID(),
                email,
                BCrypt.hashpw(password, BCrypt.gensalt()),
                false,
                RoleBuilder.aFreeUserRoleBuilder().build(),
                Instant.now(ClockConfig.utcClock())
        );
        when(loadUserPort.loadUserByEmail(email)).thenReturn(Optional.of(user));

        // When
        assertThrows(UserAlreadyCreatedException.class, () -> sut.createUser(new CreateUserCommand(email, password, roleId)));
    }

    @Test
    public void createUser_whenPasswordLessThanEightCharsAndUserExists_shouldThrowPasswordMinimumCharactersException() {
        //  Given
        String email = "test@test.com";
        String password = "1234567";
        long roleId = 1L;

        // When
        assertThrows(PasswordMinimumCharactersException.class, () -> sut.createUser(new CreateUserCommand(email, password, roleId)));
    }
}
