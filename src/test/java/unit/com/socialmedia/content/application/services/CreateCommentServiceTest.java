package unit.com.socialmedia.content.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.posts.application.port.out.CreateCommentPort;
import com.socialmedia.posts.application.port.out.LoadPostPort;
import com.socialmedia.posts.application.services.CreateCommentService;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.posts.domain.commands.CreateCommentCommand;
import com.socialmedia.posts.domain.exceptions.CommentsLimitException;
import com.socialmedia.posts.domain.exceptions.PostNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import unit.com.socialmedia.accounts.domain.RoleBuilder;
import unit.com.socialmedia.accounts.domain.UserBuilder;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateCommentServiceTest {
    private CreateCommentService sut;

    @Mock
    private LoadUserUseCase loadUserUseCase;
    @Mock
    private LoadPostPort loadPostPort;
    @Mock
    private CreateCommentPort createCommentPort;
    @Captor
    ArgumentCaptor<Comment> commentCaptor;

    private static final UUID USER_ID = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new CreateCommentService(loadUserUseCase, loadPostPort, createCommentPort);
    }

    @Test
    public void createComment_whenUserExistsAndRoleExistsAndPostExistsAndCommentsLessThanLimit_shouldCreateComment() {
        // Given
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        CreateCommentCommand command = new CreateCommentCommand(
                USER_ID,
                postId,
                commentBody
        );

        User user = UserBuilder.aRandomUserBuilder().withUserId(USER_ID).withRole(RoleBuilder.aFreeUserRoleBuilder().build()).build();
        when(loadUserUseCase.loadUserById(USER_ID)).thenReturn(Optional.of(user));

        String postBody = "postBody";
        Post post = new Post(
                postId,
                USER_ID,
                postBody,
                Instant.now(ClockConfig.utcClock()),
                Collections.emptyList()
        );
        when(loadPostPort.loadPostById(postId)).thenReturn(Optional.of(post));

        // When
        sut.createComment(command);

        // Then
        verify(createCommentPort).createNewComment(commentCaptor.capture());
        assertNotNull(commentCaptor.getValue());
        assertEquals(USER_ID, commentCaptor.getValue().getUserId());
        assertEquals(postId, commentCaptor.getValue().getPostId());
        assertNotNull(commentCaptor.getValue().getId());
        assertEquals(commentBody, commentCaptor.getValue().getBody());
    }

    @Test
    public void createComment_whenUserExistsAndRoleExistsAndPostExistsAndCommentsMoreThanLimit_shouldThrowCommentsLimitException() {
        // Given
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        CreateCommentCommand command = new CreateCommentCommand(
                USER_ID,
                postId,
                commentBody
        );

        User user = UserBuilder.aRandomUserBuilder().withUserId(USER_ID).withRole(RoleBuilder.aFreeUserRoleBuilder().build()).build();
        when(loadUserUseCase.loadUserById(USER_ID)).thenReturn(Optional.of(user));

        String postBody = "postBody";
        Comment previousComment = new Comment(
                UUID.randomUUID(),
                USER_ID,
                postId,
                "previousCommentBody",
                Instant.now(ClockConfig.utcClock())
        );
        Post post = new Post(
                postId,
                USER_ID,
                postBody,
                Instant.now(ClockConfig.utcClock()),
                Collections.nCopies(5, previousComment)
        );
        when(loadPostPort.loadPostById(postId)).thenReturn(Optional.of(post));

        // When
        assertThrows(CommentsLimitException.class, () -> sut.createComment(command));
    }

    @Test
    public void createComment_whenUserExistsAndRoleExistsAndPostDoesNotExist_shouldThrowPostNotFoundException() {
        // Given
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        CreateCommentCommand command = new CreateCommentCommand(
                USER_ID,
                postId,
                commentBody
        );

        User user = UserBuilder.aRandomUserBuilder().withUserId(USER_ID).withRole(RoleBuilder.aFreeUserRoleBuilder().build()).build();
        when(loadUserUseCase.loadUserById(USER_ID)).thenReturn(Optional.of(user));


        when(loadPostPort.loadPostById(postId)).thenReturn(Optional.empty());

        // When
        assertThrows(PostNotFoundException.class, () -> sut.createComment(command));
    }

    @Test
    public void createComment_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        // Given
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        CreateCommentCommand command = new CreateCommentCommand(
                USER_ID,
                postId,
                commentBody
        );

        when(loadUserUseCase.loadUserById(USER_ID)).thenReturn(Optional.empty());

        // When
        assertThrows(UserNotFoundException.class, () -> sut.createComment(command));
    }
}
