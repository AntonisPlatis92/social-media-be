package integration.com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.adapter.out.*;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.out.VerifyUserPort;
import com.socialmedia.accounts.application.services.LoadUserService;
import com.socialmedia.accounts.application.services.VerifyUserService;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.commands.VerifyUserCommand;
import integration.com.socialmedia.config.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import unit.com.socialmedia.accounts.domain.UserBuilder;

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
        createUserPort = new CreateUserJpaAdapter();
        loadUserPort = new LoadUserJpaAdapter();
        verifyUserPort = new VerifyUserJpaAdapter();
        verifyUserService = new VerifyUserService(loadUserPort, verifyUserPort);
        loadUserService = new LoadUserService(loadUserPort);
    }

    @Test
    public void verifyUser_whenNewUser_shouldLoadUserWithVerifiedTrue() {
        //  Given
        UUID userId = UUID.randomUUID();
        String email = "test@test.com";

        User user = UserBuilder.aRandomUserBuilder()
                .withUserId(userId)
                .withEmail(email)
                .withVerified(false)
                .build();


        // When
        createUserPort.createUser(user);
        verifyUserService.verifyUser(new VerifyUserCommand(email));

        // Then
        Optional<User> maybeUser = loadUserService.loadUserById(userId);
        assertTrue(maybeUser.isPresent());
        assertTrue(maybeUser.get().isVerified());
    }
}
