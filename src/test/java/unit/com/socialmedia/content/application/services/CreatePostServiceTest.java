package unit.com.socialmedia.content.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.posts.application.port.in.FollowingPostsMemoryUseCase;
import com.socialmedia.posts.application.port.out.CreatePostPort;
import com.socialmedia.posts.application.services.CreatePostService;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.posts.domain.commands.CreatePostCommand;
import com.socialmedia.posts.domain.exceptions.PostCharsLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import unit.com.socialmedia.accounts.domain.RoleBuilder;
import unit.com.socialmedia.accounts.domain.UserBuilder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreatePostServiceTest {
    private CreatePostService sut;

    @Mock
    private LoadUserUseCase loadUserUseCase;
    @Mock
    private CreatePostPort createPostPort;
    @Mock
    private FollowingPostsMemoryUseCase followingPostsCacheUseCase;
    @Captor
    ArgumentCaptor<Post> postCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new CreatePostService(loadUserUseCase, createPostPort, followingPostsCacheUseCase);
    }

    @Test
    public void createPost_whenUserExistsAndRoleExistsAndPostLessThanLimit_shouldCreatePost() {
        // Given
        UUID userId = UUID.randomUUID();
        String postBody = "testBody";
        CreatePostCommand command = new CreatePostCommand(
                userId,
                postBody
        );

        User user = UserBuilder.aRandomUserBuilder().withRole(RoleBuilder.aFreeUserRoleBuilder().build()).build();
        when(loadUserUseCase.loadUserById(userId)).thenReturn(Optional.of(user));

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
        CreatePostCommand command = new CreatePostCommand(
                userId,
                postBody
        );

        User user = UserBuilder.aRandomUserBuilder().withRole(RoleBuilder.aFreeUserRoleBuilder().withPostCharsLimit(1L).build()).build();
        when(loadUserUseCase.loadUserById(userId)).thenReturn(Optional.of(user));


        // Then
        assertThrows(PostCharsLimitException.class, () -> sut.createPost(command));
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

        when(loadUserUseCase.loadUserById(userId)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> sut.createPost(command));
    }
}
