package com.socialmedia.content.application.services;

import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.RoleNotFoundException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.content.application.port.out.CreateCommentPort;
import com.socialmedia.content.application.port.out.LoadCommentPort;
import com.socialmedia.content.application.port.out.LoadPostPort;
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

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.socialmedia.accounts.domain.RoleBuilder.aFreeUserRoleBuilder;
import static com.socialmedia.accounts.domain.UserBuilder.aRandomUserBuilder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateCommentServiceTest {
    private CreateCommentService sut;

    @Mock
    private LoadUserPort loadUserPort;
    @Mock
    private LoadRolePort loadRolePort;
    @Mock
    private LoadPostPort loadPostPort;
    @Mock
    private LoadCommentPort loadCommentPort;
    @Mock
    private CreateCommentPort createCommentPort;
    @Captor
    ArgumentCaptor<Comment> commentCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new CreateCommentService(loadUserPort, loadRolePort, loadPostPort, loadCommentPort, createCommentPort);
    }

    @Test
    public void createComment_whenUserExistsAndRoleExistsAndPostExistsAndCommentsLessThanLimit_shouldCreateComment() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        Long freeUserRoleId = 1L;
        CreateCommentCommand command = new CreateCommentCommand(
                userId,
                postId,
                commentBody
        );

        User user = aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.of(user));

        Role role = aFreeUserRoleBuilder().build();
        when(loadRolePort.loadRoleById(freeUserRoleId)).thenReturn(Optional.of(role));

        String postBody = "postBody";
        Post post = new Post(
                postId,
                userId,
                postBody,
                Instant.now(ClockConfig.utcClock())
        );
        when(loadPostPort.loadPostById(postId)).thenReturn(Optional.of(post));
        when(loadCommentPort.loadCommentByUserIdAndPostId(userId, postId)).thenReturn(Collections.emptyList());

        // When
        sut.createComment(command);

        // Then
        verify(createCommentPort).createNewComment(commentCaptor.capture());
        assertNotNull(commentCaptor.getValue());
        assertEquals(userId, commentCaptor.getValue().getUserId());
        assertEquals(postId, commentCaptor.getValue().getPostId());
        assertNotNull(commentCaptor.getValue().getId());
        assertEquals(commentBody, commentCaptor.getValue().getBody());
    }

    @Test
    public void createComment_whenUserExistsAndRoleExistsAndPostExistsAndCommentsMoreThanLimit_shouldThrowCommentsLimitException() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        Long freeUserRoleId = 1L;
        CreateCommentCommand command = new CreateCommentCommand(
                userId,
                postId,
                commentBody
        );

        User user = aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.of(user));

        Role role = aFreeUserRoleBuilder().build();
        when(loadRolePort.loadRoleById(freeUserRoleId)).thenReturn(Optional.of(role));

        String postBody = "postBody";
        Post post = new Post(
                postId,
                userId,
                postBody,
                Instant.now(ClockConfig.utcClock())
        );
        when(loadPostPort.loadPostById(postId)).thenReturn(Optional.of(post));
        Comment previousComment = new Comment(
                UUID.randomUUID(),
                userId,
                postId,
                "previousCommentBody",
                Instant.now(ClockConfig.utcClock())
        );
        when(loadCommentPort.loadCommentByUserIdAndPostId(userId, postId))
                .thenReturn(Collections.nCopies(5, previousComment));

        // When
        assertThrows(CommentsLimitException.class, () -> sut.createComment(command));
    }

    @Test
    public void createComment_whenUserExistsAndRoleExistsAndPostDoesNotExist_shouldThrowPostNotFoundException() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        Long freeUserRoleId = 1L;
        CreateCommentCommand command = new CreateCommentCommand(
                userId,
                postId,
                commentBody
        );

        User user = aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.of(user));

        Role role = aFreeUserRoleBuilder().build();
        when(loadRolePort.loadRoleById(freeUserRoleId)).thenReturn(Optional.of(role));

        when(loadPostPort.loadPostById(postId)).thenReturn(Optional.empty());

        // When
        assertThrows(PostNotFoundException.class, () -> sut.createComment(command));
    }

    @Test
    public void createComment_whenUserExistsAndRoleDoesNotExist_shouldThrowRoleNotFoundException() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        Long freeUserRoleId = 1L;
        CreateCommentCommand command = new CreateCommentCommand(
                userId,
                postId,
                commentBody
        );

        User user = aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.of(user));

        when(loadRolePort.loadRoleById(freeUserRoleId)).thenReturn(Optional.empty());

        // When
        assertThrows(RoleNotFoundException.class, () -> sut.createComment(command));
    }

    @Test
    public void createComment_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        String commentBody = "testBody";
        CreateCommentCommand command = new CreateCommentCommand(
                userId,
                postId,
                commentBody
        );

        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.empty());

        // When
        assertThrows(UserNotFoundException.class, () -> sut.createComment(command));
    }
}
