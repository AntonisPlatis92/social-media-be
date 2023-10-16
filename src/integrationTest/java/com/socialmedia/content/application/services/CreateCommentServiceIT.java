package com.socialmedia.content.application.services;

import com.socialmedia.accounts.adapter.out.database.CreateUserAdapter;
import com.socialmedia.accounts.adapter.out.database.LoadRoleAdapter;
import com.socialmedia.accounts.adapter.out.database.LoadUserAdapter;
import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.application.services.LoadUserService;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.config.IntegrationTestConfig;
import com.socialmedia.content.adapter.out.CreateCommentAdapter;
import com.socialmedia.content.adapter.out.CreatePostAdapter;
import com.socialmedia.content.adapter.out.LoadCommentAdapter;
import com.socialmedia.content.adapter.out.LoadPostAdapter;
import com.socialmedia.content.application.port.in.CreatePostUseCase;
import com.socialmedia.content.application.port.out.CreateCommentPort;
import com.socialmedia.content.application.port.out.CreatePostPort;
import com.socialmedia.content.application.port.out.LoadCommentPort;
import com.socialmedia.content.application.port.out.LoadPostPort;
import com.socialmedia.content.domain.Comment;
import com.socialmedia.content.domain.commands.CreateCommentCommand;
import com.socialmedia.content.domain.commands.CreatePostCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
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
    private LoadCommentPort loadCommentPort;
    private CreateCommentPort createCommentPort;


    @BeforeEach
    public void setup() {
        loadUserPort = new LoadUserAdapter();
        loadRolePort = new LoadRoleAdapter();
        createPostPort = new CreatePostAdapter();
        loadPostPort = new LoadPostAdapter();
        createUserPort = new CreateUserAdapter();
        loadCommentPort = new LoadCommentAdapter();
        createCommentPort = new CreateCommentAdapter();
        createUserUseCase = new CreateUserService(createUserPort, loadUserPort);
        loadUserUseCase = new LoadUserService(loadUserPort);
        createPostUseCase = new CreatePostService(loadUserPort, loadRolePort, createPostPort);
        createCommentService = new CreateCommentService(loadUserPort, loadRolePort, loadPostPort, loadCommentPort, createCommentPort);
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
        UUID postId = loadPostPort.loadPostByUserId(userId).get(0).getId();
        createCommentService.createComment(new CreateCommentCommand(userId, postId, commentBody));

        // Then
        List<Comment> userCommentsInPost = loadCommentPort.loadCommentByUserIdAndPostId(userId, postId);
        assertFalse(userCommentsInPost.isEmpty());
        assertEquals(1, userCommentsInPost.size());
        assertEquals(userId, userCommentsInPost.get(0).getUserId());
        assertEquals(postId, userCommentsInPost.get(0).getPostId());
        assertEquals(commentBody, userCommentsInPost.get(0).getBody());
    }
}
