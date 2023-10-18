package integration.com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.adapter.out.CreateFollowAdapter;
import com.socialmedia.accounts.adapter.out.CreateUserAdapter;
import com.socialmedia.accounts.adapter.out.LoadUserAdapter;
import com.socialmedia.accounts.application.port.out.CreateFollowPort;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.services.CreateFollowService;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.commands.CreateFollowCommand;
import integration.com.socialmedia.config.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import unit.com.socialmedia.accounts.domain.UserBuilder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(IntegrationTestConfig.class)
public class CreateFollowServiceIT {
    private CreateFollowService sut;

    private LoadUserPort loadUserPort;
    private CreateUserPort createUserPort;
    private CreateFollowPort createFollowPort;


    @BeforeEach
    public void setup() {
        createUserPort = new CreateUserAdapter();
        loadUserPort = new LoadUserAdapter();
        createFollowPort = new CreateFollowAdapter();
        sut = new CreateFollowService(loadUserPort, createFollowPort);
    }

    @Test
    public void createFollow_whenLoadUser_shouldGetFollowersAndFollowing() {
        //  Given
        UUID followerUserId = UUID.randomUUID();
        String followerUserEmail = "user1@test.com";
        UUID followingUserId = UUID.randomUUID();
        String followingUserEmail = "user2@test.com";
        User followerUser = UserBuilder.aRandomUserBuilder()
                .withUserId(followerUserId)
                .withEmail(followerUserEmail)
                .build();
        User followingUser = UserBuilder.aRandomUserBuilder()
                .withUserId(followingUserId)
                .withEmail(followingUserEmail)
                .build();

        // When
        createUserPort.createUser(followerUser);
        createUserPort.createUser(followingUser);
        sut.createNewFollow(new CreateFollowCommand(followerUserEmail, followingUserEmail));

        // Then
        User followerUserFromDb = loadUserPort.loadUserById(followerUserId).get();
        User followingUserFromDb = loadUserPort.loadUserById(followingUserId).get();

        assertFalse(followerUserFromDb.getFollowing().isEmpty());
        assertTrue(followerUserFromDb.getFollowers().isEmpty());
        assertEquals(1, followerUserFromDb.getFollowing().size());
        assertEquals(followerUserId, followerUserFromDb.getFollowing().get(0).getFollowerId());
        assertEquals(followingUserId, followerUserFromDb.getFollowing().get(0).getFollowingId());

        assertFalse(followingUserFromDb.getFollowers().isEmpty());
        assertTrue(followingUserFromDb.getFollowing().isEmpty());
        assertEquals(1, followingUserFromDb.getFollowers().size());
        assertEquals(followerUserId, followingUserFromDb.getFollowers().get(0).getFollowerId());
        assertEquals(followingUserId, followingUserFromDb.getFollowers().get(0).getFollowingId());
    }
}
