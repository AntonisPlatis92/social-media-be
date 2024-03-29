package integration.com.socialmedia.accounts.application.services;


import com.socialmedia.accounts.adapter.out.*;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import integration.com.socialmedia.config.IntegrationTestConfig;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(IntegrationTestConfig.class)
class CreateUserServiceIT {

    private CreateUserService sut;

    private LoadUserPort loadUserPort;
    private LoadRolePort loadRolePort;
    private CreateUserPort createUserPort;


    @BeforeEach
    public void setup() {
        loadUserPort = new LoadUserJpaAdapter();
        createUserPort = new CreateUserJpaAdapter();
        loadRolePort = new LoadRoleJpaAdapter();
        sut = new CreateUserService(loadUserPort, loadRolePort, createUserPort);
    }

    @Test
    public void createUser_whenLoadUser_shouldMatchFields() {
        //  Given
        String email = "test@test.com";
        String password = "12345678";
        long roleId = 1L;

        // When
        sut.createUser(new CreateUserCommand(email, password, roleId));

        // Then
        Optional<User> maybeUserCreated = loadUserPort.loadUserByEmail(email);
        assertTrue(maybeUserCreated.isPresent());
        assertEquals(email, maybeUserCreated.get().getEmail());
        assertNotNull(maybeUserCreated.get().getHashedPassword());
    }
}
