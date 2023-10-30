package integration.com.socialmedia.content.adapter.out;

import com.socialmedia.accounts.adapter.out.CreateUserJpaAdapter;
import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.out.CreateCommentAdapter;
import com.socialmedia.posts.adapter.out.CreatePostJpaAdapter;
import com.socialmedia.posts.adapter.out.LoadCommentsOnOwnPostsJpaAdapter;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.posts.domain.commands.CreateCommentCommand;
import com.socialmedia.posts.domain.commands.CreatePostCommand;
import integration.com.socialmedia.config.IntegrationTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import unit.com.socialmedia.accounts.domain.UserBuilder;

import java.util.List;
import java.util.UUID;

@ExtendWith(IntegrationTestConfig.class)
public class LoadCommentsOnOwnPostsJpaAdapterIT {
    private LoadCommentsOnOwnPostsJpaAdapter sut;
    private CreateUserJpaAdapter createUserJpaAdapter;
    private CreatePostJpaAdapter createPostJpaAdapter;
    private CreateCommentAdapter createCommentAdapter;

    @BeforeEach
    public void setup() {
        createCommentAdapter = new CreateCommentAdapter();
        createPostJpaAdapter = new CreatePostJpaAdapter();
        createUserJpaAdapter = new CreateUserJpaAdapter();
        sut = new LoadCommentsOnOwnPostsJpaAdapter();
    }

    @Test
    public void loadCommentsOnOwnPosts_whenUserExists_shouldReturnComments() {
        //  Given
        UUID userId = UUID.randomUUID();

        //  When
        createUserJpaAdapter.createUser(UserBuilder.aRandomUserBuilder().withUserId(userId).build());
        Post post = Post.createPostFromCommand(new CreatePostCommand(userId, "test"));
        createPostJpaAdapter.createNewPost(post);
        createCommentAdapter.createNewComment(Comment.createCommentFromCommand(new CreateCommentCommand(userId, post.getId(), "test")));
        List<CommentReturnVM> commentReturnVM = sut.loadCommentsOnOwnPosts(userId);

        //  Then
        Assertions.assertEquals(1, commentReturnVM.size());
        Assertions.assertEquals("test", commentReturnVM.get(0).commentBody());
    }
}
