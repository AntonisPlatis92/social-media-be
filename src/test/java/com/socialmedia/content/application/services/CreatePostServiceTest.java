package com.socialmedia.content.application.services;

import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.RoleNotFoundException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.content.application.port.out.CreatePostPort;
import com.socialmedia.content.domain.Post;
import com.socialmedia.content.domain.commands.CreatePostCommand;
import com.socialmedia.content.domain.exceptions.PostCharsLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static com.socialmedia.accounts.domain.RoleBuilder.aFreeUserRoleBuilder;
import static com.socialmedia.accounts.domain.UserBuilder.aRandomUserBuilder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreatePostServiceTest {
    private CreatePostService sut;

    @Mock
    private LoadUserPort loadUserPort;
    @Mock
    private LoadRolePort loadRolePort;
    @Mock
    private CreatePostPort createPostPort;
    @Captor
    ArgumentCaptor<Post> postCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new CreatePostService(loadUserPort, loadRolePort, createPostPort);
    }

    @Test
    public void createPost_whenUserExistsAndRoleExistsAndPostLessThanLimit_shouldCreatePost() {
        // Given
        UUID userId = UUID.randomUUID();
        String postBody = "testBody";
        Long freeUserRoleId = 1L;
        CreatePostCommand command = new CreatePostCommand(
               userId,
               postBody
        );

        User user = aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.of(user));

        Role role = aFreeUserRoleBuilder().build();
        when(loadRolePort.loadRoleById(freeUserRoleId)).thenReturn(Optional.of(role));


        // When
        sut.createPost(command);

        // Then
        verify(createPostPort).createNewPost(postCaptor.capture());
        assertNotNull(postCaptor.getValue());
        assertEquals(userId, postCaptor.getValue().getUserId());
        assertNotNull(postCaptor.getValue().getId());
        assertEquals(postBody, postCaptor.getValue().getBody());
    }

    @Test
    public void createPost_whenUserExistsAndRoleExistsAndPostMoreThanLimit_shouldThrowPostCharsLimitException() {
        // Given
        UUID userId = UUID.randomUUID();
        String postBody = "testBody";
        Long freeUserRoleId = 1L;
        CreatePostCommand command = new CreatePostCommand(
                userId,
                postBody
        );

        User user = aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.of(user));

        Role role = aFreeUserRoleBuilder().withPostCharsLimit(1L).build();
        when(loadRolePort.loadRoleById(freeUserRoleId)).thenReturn(Optional.of(role));


        // Then
        assertThrows(PostCharsLimitException.class, () -> sut.createPost(command));
    }

    @Test
    public void createPost_whenUserExistsAndRoleDoesNotExist_shouldThrowRoleNotFoundException() {
        // Given
        UUID userId = UUID.randomUUID();
        String postBody = "testBody";
        Long roleId = 3L;
        CreatePostCommand command = new CreatePostCommand(
                userId,
                postBody
        );

        User user = aRandomUserBuilder().withRoleId(roleId).build();
        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.of(user));

        when(loadRolePort.loadRoleById(roleId)).thenReturn(Optional.empty());


        // Then
        assertThrows(RoleNotFoundException.class, () -> sut.createPost(command));
    }

    @Test
    public void createPost_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        // Given
        UUID userId = UUID.randomUUID();
        String postBody = "testBody";
        CreatePostCommand command = new CreatePostCommand(
                userId,
                postBody
        );

        when(loadUserPort.loadUserById(userId)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> sut.createPost(command));
    }
}
