package integration.com.socialmedia.content.adapter.out;

import com.socialmedia.accounts.adapter.out.CreateUserJpaAdapter;
import com.socialmedia.posts.adapter.in.vms.OwnPostReturnVM;
import com.socialmedia.posts.adapter.out.CreatePostJpaAdapter;
import com.socialmedia.posts.adapter.out.LoadOwnPostsJpaAdapter;
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
public class LoadOwnPostsJpaAdapterIT {
    private LoadOwnPostsJpaAdapter sut;
    private CreateUserJpaAdapter createUserJpaAdapter;
    private CreatePostJpaAdapter createPostJpaAdapter;

    @BeforeEach
    public void setup() {
        createUserJpaAdapter = new CreateUserJpaAdapter();
        createPostJpaAdapter = new CreatePostJpaAdapter();
        sut = new LoadOwnPostsJpaAdapter();
    }

    @Test
    public void loadOwnPosts_whenUserExists_shouldReturnPosts() {
        //  Given
        UUID userId = UUID.randomUUID();
        String postBody = "test";


        //  When
        createUserJpaAdapter.createUser(UserBuilder.aRandomUserBuilder().withUserId(userId).build());
        createPostJpaAdapter.createNewPost(Post.createPostFromCommand(new CreatePostCommand(userId, postBody)));
        List<OwnPostReturnVM> ownPostReturnVM = sut.loadOwnPosts(userId);

        //  Then
        Assertions.assertEquals(1, ownPostReturnVM.size());
        Assertions.assertEquals(postBody, ownPostReturnVM.get(0).postBody());
    }
}
