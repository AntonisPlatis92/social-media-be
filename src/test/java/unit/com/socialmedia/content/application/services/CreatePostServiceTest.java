package unit.com.socialmedia.content.application.services;

import com.socialmedia.accounts.application.port.in.LoadRoleUseCase;
import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.RoleNotFoundException;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreatePostServiceTest {
    private CreatePostService sut;

    @Mock
    private LoadUserUseCase loadUserUseCase;
    @Mock
    private LoadRoleUseCase loadRoleUseCase;
    @Mock
    private CreatePostPort createPostPort;
    @Captor
    ArgumentCaptor<Post> postCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new CreatePostService(loadUserUseCase, loadRoleUseCase, createPostPort);
    }

    @Test
    public void createPost_whenUserExistsAndRoleExistsAndPostLessThanLimit_shouldCreatePost() {
        // Given
        String userEmail = "test@test.com";
        String postBody = "testBody";
        Long freeUserRoleId = 1L;
        CreatePostCommand command = new CreatePostCommand(
                userEmail,
                postBody
        );

        User user = UserBuilder.aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserUseCase.loadUserByEmail(userEmail)).thenReturn(Optional.of(user));

        Role role = RoleBuilder.aFreeUserRoleBuilder().build();
        when(loadRoleUseCase.loadRole(freeUserRoleId)).thenReturn(Optional.of(role));


        // When
        sut.createPost(command);

        // Then
        verify(createPostPort).createNewPost(postCaptor.capture());
        assertNotNull(postCaptor.getValue());
        assertEquals(userEmail, postCaptor.getValue().getUserEmail());
        assertNotNull(postCaptor.getValue().getId());
        assertEquals(postBody, postCaptor.getValue().getBody());
    }

    @Test
    public void createPost_whenUserExistsAndRoleExistsAndPostMoreThanLimit_shouldThrowPostCharsLimitException() {
        // Given
        String userEmail = "test@test.com";
        String postBody = "testBody";
        Long freeUserRoleId = 1L;
        CreatePostCommand command = new CreatePostCommand(
                userEmail,
                postBody
        );

        User user = UserBuilder.aRandomUserBuilder().withRoleId(freeUserRoleId).build();
        when(loadUserUseCase.loadUserByEmail(userEmail)).thenReturn(Optional.of(user));

        Role role = RoleBuilder.aFreeUserRoleBuilder().withPostCharsLimit(1L).build();
        when(loadRoleUseCase.loadRole(freeUserRoleId)).thenReturn(Optional.of(role));


        // Then
        assertThrows(PostCharsLimitException.class, () -> sut.createPost(command));
    }

    @Test
    public void createPost_whenUserExistsAndRoleDoesNotExist_shouldThrowRoleNotFoundException() {
        // Given
        String userEmail = "test@test.com";
        String postBody = "testBody";
        Long roleId = 3L;
        CreatePostCommand command = new CreatePostCommand(
                userEmail,
                postBody
        );

        User user = UserBuilder.aRandomUserBuilder().withRoleId(roleId).build();
        when(loadUserUseCase.loadUserByEmail(userEmail)).thenReturn(Optional.of(user));

        when(loadRoleUseCase.loadRole(roleId)).thenReturn(Optional.empty());


        // Then
        assertThrows(RoleNotFoundException.class, () -> sut.createPost(command));
    }

    @Test
    public void createPost_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        // Given
        String userEmail = "test@test.com";
        String postBody = "testBody";
        CreatePostCommand command = new CreatePostCommand(
                userEmail,
                postBody
        );

        when(loadUserUseCase.loadUserByEmail(userEmail)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> sut.createPost(command));
    }
}
