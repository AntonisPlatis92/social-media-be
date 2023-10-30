package integration.com.socialmedia.content.adapter.out;

import com.socialmedia.accounts.adapter.out.CreateFollowJpaAdapter;
import com.socialmedia.accounts.adapter.out.CreateUserJpaAdapter;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.out.CreateCommentAdapter;
import com.socialmedia.posts.adapter.out.CreatePostJpaAdapter;
import com.socialmedia.posts.adapter.out.LoadCommentsOnOwnAndFollowingPostsJpaAdapter;
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
public class LoadCommentsOnOwnAndFollowingPostsJpaAdapterIT {
    private LoadCommentsOnOwnAndFollowingPostsJpaAdapter sut;
    private CreateUserJpaAdapter createUserJpaAdapter;
    private CreateFollowJpaAdapter createFollowJpaAdapter;
    private CreatePostJpaAdapter createPostJpaAdapter;
    private CreateCommentAdapter createCommentAdapter;

    @BeforeEach
    public void setup() {
        createFollowJpaAdapter = new CreateFollowJpaAdapter();
        createCommentAdapter = new CreateCommentAdapter();
        createPostJpaAdapter = new CreatePostJpaAdapter();
        createUserJpaAdapter = new CreateUserJpaAdapter();
        sut = new LoadCommentsOnOwnAndFollowingPostsJpaAdapter();
    }

    @Test
    public void loadCommentsOnOwnAndFollowingPosts_whenUserExists_shouldReturnComments() {
        //  Given
        UUID userId = UUID.randomUUID();
        UUID followingUserId = UUID.randomUUID();
        String userEmail = "test@test.com";
        String followingUserEmail = "test2@test.com";

        //  When
        createUserJpaAdapter.createUser(UserBuilder.aRandomUserBuilder().withUserId(userId).withEmail(userEmail).build());
        createUserJpaAdapter.createUser(UserBuilder.aRandomUserBuilder().withUserId(followingUserId).withEmail(followingUserEmail).build());
        createFollowJpaAdapter.createFollow(Follow.createFollowFromFollowerIdAndFollowingId(userId, followingUserId));
        Post post1 = Post.createPostFromCommand(new CreatePostCommand(userId, "postBody1"));
        createPostJpaAdapter.createNewPost(post1);
        Post post2 = Post.createPostFromCommand(new CreatePostCommand(followingUserId, "postBody2"));
        createPostJpaAdapter.createNewPost(post2);
        createCommentAdapter.createNewComment(Comment.createCommentFromCommand(new CreateCommentCommand(userId, post1.getId(), "commentBody1")));
        createCommentAdapter.createNewComment(Comment.createCommentFromCommand(new CreateCommentCommand(followingUserId, post2.getId(), "commentBody2")));
        List<CommentReturnVM> commentsReturnVM = sut.loadCommentsOnOwnAndFollowingPosts(userId);

        //  Then
        Assertions.assertEquals(2, commentsReturnVM.size());
        Assertions.assertEquals("commentBody2", commentsReturnVM.get(0).commentBody());
        Assertions.assertEquals("commentBody1", commentsReturnVM.get(1).commentBody());
    }
}
