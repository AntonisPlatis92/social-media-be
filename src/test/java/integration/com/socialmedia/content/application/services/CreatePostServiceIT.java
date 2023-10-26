package integration.com.socialmedia.content.application.services;

import com.socialmedia.accounts.adapter.out.CreateUserAdapter;
import com.socialmedia.accounts.adapter.out.LoadRoleAdapter;
import com.socialmedia.accounts.adapter.out.LoadUserAdapter;
import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.application.services.LoadUserService;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.posts.adapter.out.FollowingPostsRedisAdapter;
import com.socialmedia.posts.adapter.out.LoadFollowingPostsAdapter;
import com.socialmedia.posts.application.port.in.FollowingPostsCacheUseCase;
import com.socialmedia.posts.application.port.out.FollowingPostsCachePort;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;
import com.socialmedia.posts.application.services.FollowingPostsCacheService;
import integration.com.socialmedia.config.IntegrationTestConfig;
import com.socialmedia.posts.adapter.out.CreatePostAdapter;
import com.socialmedia.posts.adapter.out.LoadPostAdapter;
import com.socialmedia.posts.application.port.out.CreatePostPort;
import com.socialmedia.posts.application.port.out.LoadPostPort;
import com.socialmedia.posts.application.services.CreatePostService;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.posts.domain.commands.CreatePostCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(IntegrationTestConfig.class)
public class CreatePostServiceIT {
    private CreatePostService createPostService;
    private CreateUserUseCase createUserUseCase;
    private LoadUserUseCase loadUserUseCase;
    private LoadUserPort loadUserPort;
    private CreateUserPort createUserPort;
    private LoadRolePort loadRolePort;
    private CreatePostPort createPostPort;
    private LoadPostPort loadPostPort;
    private FollowingPostsCachePort followingPostsCachePort;
    private LoadFollowingPostsPort loadFollowingPostsPort;
    private FollowingPostsCacheUseCase followingPostsCacheUseCase;


    @BeforeEach
    public void setup() {
        loadUserPort = new LoadUserAdapter();
        loadRolePort = new LoadRoleAdapter();
        createPostPort = new CreatePostAdapter();
        loadPostPort = new LoadPostAdapter();
        createUserPort = new CreateUserAdapter();
        createUserUseCase = new CreateUserService(loadUserPort, loadRolePort, createUserPort);
        loadUserUseCase = new LoadUserService(loadUserPort);
        loadFollowingPostsPort = new LoadFollowingPostsAdapter();
        followingPostsCachePort = new FollowingPostsRedisAdapter();
        followingPostsCacheUseCase = new FollowingPostsCacheService(loadFollowingPostsPort, followingPostsCachePort, loadUserUseCase);
        createPostService = new CreatePostService(loadUserUseCase, createPostPort, followingPostsCacheUseCase);
    }

    @Test
    public void createPost_whenLoadPost_shouldMatchFields() {
        //  Given
        String userEmail = "testEmail";
        String userPassword = "testPassword";
        long userRoleId = 1L;
        String postBody = "testPostBody";

        // When
        createUserUseCase.createUser(new CreateUserCommand(userEmail, userPassword, userRoleId));
        UUID userId = loadUserUseCase.loadUserByEmail(userEmail).get().getUserId();
        createPostService.createPost(new CreatePostCommand(userId, postBody));

        // Then
        List<Post> userPosts = loadPostPort.loadPostsByUserId(userId);
        assertFalse(userPosts.isEmpty());
        assertEquals(1, userPosts.size());
        assertEquals(userId, userPosts.get(0).getUserId());
        assertEquals(postBody, userPosts.get(0).getBody());
    }
}
