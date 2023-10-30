package integration.com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.out.CreateFollowJpaAdapter;
import com.socialmedia.accounts.adapter.out.CreateUserJpaAdapter;
import com.socialmedia.accounts.adapter.out.LoadUserJpaAdapter;
import com.socialmedia.accounts.adapter.out.RemoveFollowJpaAdapter;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.accounts.domain.User;
import integration.com.socialmedia.config.IntegrationTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import unit.com.socialmedia.accounts.domain.UserBuilder;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

@ExtendWith(IntegrationTestConfig.class)
public class RemoveFollowJpaAdapterIT {
    private RemoveFollowJpaAdapter sut;
    private CreateFollowJpaAdapter createFollowJpaAdapter;
    private CreateUserJpaAdapter createUserJpaAdapter;
    private LoadUserJpaAdapter loadUserJpaAdapter;

    @BeforeEach
    public void setup() {
        createUserJpaAdapter = new CreateUserJpaAdapter();
        loadUserJpaAdapter = new LoadUserJpaAdapter();
        createFollowJpaAdapter = new CreateFollowJpaAdapter();
        sut = new RemoveFollowJpaAdapter();
    }

    @Test
    public void removeFollow_whenFollowExists_shouldRemoveFollow() {
        //  Given
        UUID userId = UUID.randomUUID();
        UUID followingUserId = UUID.randomUUID();
        String userEmail = "test@test.com";
        String followingUserEmail = "test2@test.com";

        // When
        createUserJpaAdapter.createUser(UserBuilder.aRandomUserBuilder().withUserId(userId).withEmail(userEmail).build());
        createUserJpaAdapter.createUser(UserBuilder.aRandomUserBuilder().withUserId(followingUserId).withEmail(followingUserEmail).build());
        createFollowJpaAdapter.createFollow(Follow.createFollowFromFollowerIdAndFollowingId(userId, followingUserId));
        sut.removeFollowByFollowerAndFollowingId(userId, followingUserId);
        User user = loadUserJpaAdapter.loadUserById(userId).get();

        // Then
        Assertions.assertTrue(user.getFollowing().isEmpty());
    }
}
