package integration.com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.adapter.out.CreateUserAdapter;
import com.socialmedia.accounts.adapter.out.LoadUserAdapter;
import com.socialmedia.accounts.adapter.out.VerifyUserAdapter;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.out.VerifyUserPort;
import com.socialmedia.accounts.application.services.LoadUserService;
import com.socialmedia.accounts.application.services.VerifyUserService;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.commands.VerifyUserCommand;
import com.socialmedia.config.ClockConfig;
import integration.com.socialmedia.config.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(IntegrationTestConfig.class)
public class VerifyUserServiceIT {
    private VerifyUserService verifyUserService;
    private LoadUserService loadUserService;

    private LoadUserPort loadUserPort;
    private VerifyUserPort verifyUserPort;
    private CreateUserPort createUserPort;


    @BeforeEach
    public void setup() {
        createUserPort = new CreateUserAdapter();
        loadUserPort = new LoadUserAdapter();
        verifyUserPort = new VerifyUserAdapter();
        verifyUserService = new VerifyUserService(loadUserPort, verifyUserPort);
        loadUserService = new LoadUserService(loadUserPort);
    }

    @Test
    public void verifyUser_whenNewUser_shouldLoadUserWithVerifiedTrue() {
        //  Given
        UUID userId = UUID.randomUUID();
        String email = "test@test.com";
        String hashedPassword = "testPassword";
        boolean verified = false;
        long roleId = 1L;
        Instant creationTime = Instant.now(ClockConfig.utcClock());

        User user = new User(
                userId,
                email,
                hashedPassword,
                verified,
                roleId,
                creationTime
                );


        // When
        createUserPort.createUser(user);
        verifyUserService.verifyUser(new VerifyUserCommand(email));

        // Then
        Optional<User> maybeUser = loadUserService.loadUserById(userId);
        assertTrue(maybeUser.isPresent());
        assertTrue(maybeUser.get().isVerified());
    }
}
