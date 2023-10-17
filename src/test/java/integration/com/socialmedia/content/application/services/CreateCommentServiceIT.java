package integration.com.socialmedia.content.application.services;

import com.socialmedia.accounts.adapter.out.CreateUserAdapter;
import com.socialmedia.accounts.adapter.out.LoadRoleAdapter;
import com.socialmedia.accounts.adapter.out.LoadUserAdapter;
import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.application.port.in.LoadRoleUseCase;
import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.application.services.LoadRoleService;
import com.socialmedia.accounts.application.services.LoadUserService;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.content.domain.Post;
import integration.com.socialmedia.config.IntegrationTestConfig;
import com.socialmedia.content.adapter.out.CreateCommentAdapter;
import com.socialmedia.content.adapter.out.CreatePostAdapter;
import com.socialmedia.content.adapter.out.LoadCommentAdapter;
import com.socialmedia.content.adapter.out.LoadPostAdapter;
import com.socialmedia.content.application.port.in.CreatePostUseCase;
import com.socialmedia.content.application.port.out.CreateCommentPort;
import com.socialmedia.content.application.port.out.CreatePostPort;
import com.socialmedia.content.application.port.out.LoadCommentPort;
import com.socialmedia.content.application.port.out.LoadPostPort;
import com.socialmedia.content.application.services.CreateCommentService;
import com.socialmedia.content.application.services.CreatePostService;
import com.socialmedia.content.domain.Comment;
import com.socialmedia.content.domain.commands.CreateCommentCommand;
import com.socialmedia.content.domain.commands.CreatePostCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
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
    private LoadRoleUseCase loadRoleUseCase;

    private LoadUserPort loadUserPort;
    private CreateUserPort createUserPort;
    private LoadRolePort loadRolePort;
    private CreatePostPort createPostPort;
    private LoadPostPort loadPostPort;
    private LoadCommentPort loadCommentPort;
    private CreateCommentPort createCommentPort;


    @BeforeEach
    public void setup() {
        loadUserPort = new LoadUserAdapter();
        loadRolePort = new LoadRoleAdapter();
        createPostPort = new CreatePostAdapter();
        loadPostPort = new LoadPostAdapter();
        createUserPort = new CreateUserAdapter();
        createCommentPort = new CreateCommentAdapter();
        createUserUseCase = new CreateUserService(createUserPort, loadUserPort);
        loadUserUseCase = new LoadUserService(loadUserPort);
        loadRoleUseCase = new LoadRoleService(loadRolePort);
        createPostUseCase = new CreatePostService(loadUserUseCase, loadRoleUseCase, createPostPort);
        createCommentService = new CreateCommentService(loadUserUseCase, loadRoleUseCase, loadPostPort, createCommentPort);
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
        createPostUseCase.createPost(new CreatePostCommand(userEmail, postBody));
        UUID postId = loadPostPort.loadPostsByUserEmail(userEmail).get(0).getId();
        createCommentService.createComment(new CreateCommentCommand(userEmail, postId, commentBody));

        // Then
        Optional<Post> postAfterComment = loadPostPort.loadPostById(postId);
        assertFalse(postAfterComment.get().getComments().isEmpty());
        assertEquals(1, postAfterComment.get().getComments().size());
        assertEquals(userEmail, postAfterComment.get().getComments().get(0).getUserEmail());
        assertEquals(postId, postAfterComment.get().getComments().get(0).getPostId());
        assertEquals(commentBody, postAfterComment.get().getComments().get(0).getBody());
    }
}