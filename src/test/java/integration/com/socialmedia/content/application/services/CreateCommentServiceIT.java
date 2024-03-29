package integration.com.socialmedia.content.application.services;

import com.socialmedia.accounts.adapter.out.*;
import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.application.services.LoadUserService;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.posts.adapter.out.*;
import com.socialmedia.posts.application.port.in.FollowingPostsCacheUseCase;
import com.socialmedia.posts.application.port.out.*;
import com.socialmedia.posts.application.services.FollowingPostsCacheService;
import com.socialmedia.posts.domain.Post;
import integration.com.socialmedia.config.IntegrationTestConfig;
import com.socialmedia.posts.application.port.in.CreatePostUseCase;
import com.socialmedia.posts.application.services.CreateCommentService;
import com.socialmedia.posts.application.services.CreatePostService;
import com.socialmedia.posts.domain.commands.CreateCommentCommand;
import com.socialmedia.posts.domain.commands.CreatePostCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(IntegrationTestConfig.class)
public class CreateCommentServiceIT {
    private CreateCommentService createCommentService;
    private CreatePostUseCase createPostUseCase;
    private CreateUserUseCase createUserUseCase;
    private LoadUserUseCase loadUserUseCase;
    private LoadUserPort loadUserPort;
    private CreateUserPort createUserPort;
    private LoadRolePort loadRolePort;
    private CreatePostPort createPostPort;
    private LoadPostPort loadPostPort;
    private CreateCommentPort createCommentPort;
    private FollowingPostsCachePort followingPostsCachePort;
    private FollowingPostsCacheUseCase followingPostsCacheUseCase;
    private LoadFollowingPostsPort loadFollowingPostsPort;


    @BeforeEach
    public void setup() {
        loadUserPort = new LoadUserJpaAdapter();
        loadRolePort = new LoadRoleJpaAdapter();
        createPostPort = new CreatePostJpaAdapter();
        loadPostPort = new LoadPostJpaAdapter();
        createUserPort = new CreateUserJpaAdapter();
        createCommentPort = new CreateCommentJpaAdapter();
        loadFollowingPostsPort = new LoadFollowingPostsJpaAdapter();
        followingPostsCachePort = new FollowingPostsRedisAdapter();
        createUserUseCase = new CreateUserService(loadUserPort, loadRolePort, createUserPort);
        loadUserUseCase = new LoadUserService(loadUserPort);
        followingPostsCacheUseCase = new FollowingPostsCacheService(loadFollowingPostsPort, followingPostsCachePort, loadUserUseCase);
        createPostUseCase = new CreatePostService(loadUserUseCase, createPostPort, followingPostsCacheUseCase);
        createCommentService = new CreateCommentService(loadUserUseCase, loadPostPort, createCommentPort);
    }

    @Test
    public void createComment_whenLoadComment_shouldMatchFields() {
        //  Given
        String userEmail = "testEmail";
        String userPassword = "testPassword";
        long userRoleId = 1L;
        String postBody = "testPostBody";
        String commentBody = "testCommentBody";

        // When
        createUserUseCase.createUser(new CreateUserCommand(userEmail, userPassword, userRoleId));
        UUID userId = loadUserUseCase.loadUserByEmail(userEmail).get().getUserId();
        createPostUseCase.createPost(new CreatePostCommand(userId, postBody));
        UUID postId = loadPostPort.loadPostsByUserId(userId).get(0).getId();
        createCommentService.createComment(new CreateCommentCommand(userId, postId, commentBody));

        // Then
        Optional<Post> postAfterComment = loadPostPort.loadPostById(postId);
        assertFalse(postAfterComment.get().getComments().isEmpty());
        assertEquals(1, postAfterComment.get().getComments().size());
        assertEquals(userId, postAfterComment.get().getComments().get(0).getUserId());
        assertEquals(postId, postAfterComment.get().getComments().get(0).getPostId());
        assertEquals(commentBody, postAfterComment.get().getComments().get(0).getBody());
    }
}
