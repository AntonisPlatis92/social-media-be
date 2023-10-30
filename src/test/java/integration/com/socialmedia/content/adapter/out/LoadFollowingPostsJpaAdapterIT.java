package integration.com.socialmedia.content.adapter.out;

import com.socialmedia.accounts.adapter.out.CreateFollowJpaAdapter;
import com.socialmedia.accounts.adapter.out.CreateUserJpaAdapter;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.adapter.out.CreatePostJpaAdapter;
import com.socialmedia.posts.adapter.out.LoadFollowingPostsJpaAdapter;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.posts.domain.commands.CreatePostCommand;
import integration.com.socialmedia.config.IntegrationTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import unit.com.socialmedia.accounts.domain.UserBuilder;

import java.util.List;
import java.util.UUID;

@ExtendWith(IntegrationTestConfig.class)
public class LoadFollowingPostsJpaAdapterIT {
    private LoadFollowingPostsJpaAdapter sut;
    private CreateUserJpaAdapter createUserJpaAdapter;
    private CreatePostJpaAdapter createPostJpaAdapter;

    @BeforeEach
    public void setup() {
        createUserJpaAdapter = new CreateUserJpaAdapter();
        createPostJpaAdapter = new CreatePostJpaAdapter();
        sut = new LoadFollowingPostsJpaAdapter();
    }

    @Test
    public void loadFollowingPosts_whenUserExists_shouldReturnFollowingPosts() {
        //  Given
        UUID followingUserId = UUID.randomUUID();
        String followingUserEmail = "test2@test.com";

        //  When
        createUserJpaAdapter.createUser(UserBuilder.aRandomUserBuilder().withUserId(followingUserId).withEmail(followingUserEmail).build());
        createPostJpaAdapter.createNewPost(Post.createPostFromCommand(new CreatePostCommand(followingUserId, "test")));
        List<FollowingPostsReturnVM> followingPostsReturnVM = sut.loadFollowingPostsByFollowingUserIds(List.of(followingUserId));

        //  Then
        Assertions.assertEquals(1, followingPostsReturnVM.size());
        Assertions.assertEquals("test", followingPostsReturnVM.get(0).postBody());
    }
}
