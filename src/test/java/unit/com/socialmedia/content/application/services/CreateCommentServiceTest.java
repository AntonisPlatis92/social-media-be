package unit.com.socialmedia.content.application.services;

import com.socialmedia.accounts.application.port.in.LoadRoleUseCase;
import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.RoleNotFoundException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.content.application.port.out.CreateCommentPort;
import com.socialmedia.content.application.port.out.LoadPostPort;
import com.socialmedia.content.application.services.CreateCommentService;
import com.socialmedia.content.domain.Comment;
import com.socialmedia.content.domain.Post;
import com.socialmedia.content.domain.commands.CreateCommentCommand;
import com.socialmedia.content.domain.exceptions.CommentsLimitException;
import com.socialmedia.content.domain.exceptions.PostNotFoundException;
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
    private LoadRoleUseCase loadRoleUseCase;
    @Mock
    private LoadPostPort loadPostPort;
    @Mock
    private CreateCommentPort createCommentPort;
    @Captor
    ArgumentCaptor<Comment> commentCaptor;

    private static final String USER_EMAIL = "test@test.com";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new CreateCommentService(loadUserUseCase, loadRoleUseCase, loadPostPort, createCommentPort);
    }

    @Test
    public void createComment_whenUserExistsAndRoleExistsAndPostExistsAndCommentsLessThanLimit_shouldCreateComment() {
        // Given
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        Long freeUserRoleId = 1L;
        CreateCommentCommand command = new CreateCommentCommand(
                USER_EMAIL,
                postId,
                commentBody
        );

        User user = UserBuilder.aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserUseCase.loadUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        Role role = RoleBuilder.aFreeUserRoleBuilder().build();
        when(loadRoleUseCase.loadRole(freeUserRoleId)).thenReturn(Optional.of(role));

        String postBody = "postBody";
        Post post = new Post(
                postId,
                USER_EMAIL,
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
        assertEquals(USER_EMAIL, commentCaptor.getValue().getUserEmail());
        assertEquals(postId, commentCaptor.getValue().getPostId());
        assertNotNull(commentCaptor.getValue().getId());
        assertEquals(commentBody, commentCaptor.getValue().getBody());
    }

    @Test
    public void createComment_whenUserExistsAndRoleExistsAndPostExistsAndCommentsMoreThanLimit_shouldThrowCommentsLimitException() {
        // Given
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        Long freeUserRoleId = 1L;
        CreateCommentCommand command = new CreateCommentCommand(
                USER_EMAIL,
                postId,
                commentBody
        );

        User user = UserBuilder.aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserUseCase.loadUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        Role role = RoleBuilder.aFreeUserRoleBuilder().build();
        when(loadRoleUseCase.loadRole(freeUserRoleId)).thenReturn(Optional.of(role));

        String postBody = "postBody";
        Comment previousComment = new Comment(
                UUID.randomUUID(),
                USER_EMAIL,
                postId,
                "previousCommentBody",
                Instant.now(ClockConfig.utcClock())
        );
        Post post = new Post(
                postId,
                USER_EMAIL,
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
        Long freeUserRoleId = 1L;
        CreateCommentCommand command = new CreateCommentCommand(
                USER_EMAIL,
                postId,
                commentBody
        );

        User user = UserBuilder.aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserUseCase.loadUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        Role role = RoleBuilder.aFreeUserRoleBuilder().build();
        when(loadRoleUseCase.loadRole(freeUserRoleId)).thenReturn(Optional.of(role));

        when(loadPostPort.loadPostById(postId)).thenReturn(Optional.empty());

        // When
        assertThrows(PostNotFoundException.class, () -> sut.createComment(command));
    }

    @Test
    public void createComment_whenUserExistsAndRoleDoesNotExist_shouldThrowRoleNotFoundException() {
        // Given
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        Long freeUserRoleId = 1L;
        CreateCommentCommand command = new CreateCommentCommand(
                USER_EMAIL,
                postId,
                commentBody
        );

        User user = UserBuilder.aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserUseCase.loadUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        when(loadRoleUseCase.loadRole(freeUserRoleId)).thenReturn(Optional.empty());

        // When
        assertThrows(RoleNotFoundException.class, () -> sut.createComment(command));
    }

    @Test
    public void createComment_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        // Given
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        CreateCommentCommand command = new CreateCommentCommand(
                USER_EMAIL,
                postId,
                commentBody
        );

        when(loadUserUseCase.loadUserByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        // When
        assertThrows(UserNotFoundException.class, () -> sut.createComment(command));
    }
}
