package integration.com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.adapter.out.*;
import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.application.port.out.CreateFollowPort;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.services.CreateFollowService;
import com.socialmedia.accounts.application.services.LoadUserService;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.commands.CreateFollowCommand;
import com.socialmedia.posts.adapter.out.FollowingPostsRedisAdapter;
import com.socialmedia.posts.adapter.out.LoadFollowingPostsAdapter;
import com.socialmedia.posts.adapter.out.LoadFollowingPostsJpaAdapter;
import com.socialmedia.posts.application.port.in.FollowingPostsCacheUseCase;
import com.socialmedia.posts.application.port.out.FollowingPostsCachePort;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;
import com.socialmedia.posts.application.services.FollowingPostsCacheService;
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
    private FollowingPostsCachePort followingPostsCachePort;
    private LoadFollowingPostsPort  loadFollowingPostsPort;
    private LoadUserUseCase loadUserUseCase;
    private FollowingPostsCacheUseCase followingPostsCacheUseCase;


    @BeforeEach
    public void setup() {
        createUserPort = new CreateUserJpaAdapter();
        loadUserPort = new LoadUserJpaAdapter();
        createFollowPort = new CreateFollowJpaAdapter();
        loadFollowingPostsPort = new LoadFollowingPostsJpaAdapter();
        followingPostsCachePort = new FollowingPostsRedisAdapter();
        loadUserUseCase = new LoadUserService(loadUserPort);
        followingPostsCacheUseCase = new FollowingPostsCacheService(loadFollowingPostsPort, followingPostsCachePort, loadUserUseCase);
        sut = new CreateFollowService(loadUserPort, createFollowPort, followingPostsCacheUseCase);
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
        sut.createNewFollow(new CreateFollowCommand(followerUserId, followingUserEmail));

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
