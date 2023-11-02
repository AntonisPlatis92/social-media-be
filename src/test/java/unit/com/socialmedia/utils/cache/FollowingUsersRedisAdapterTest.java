package unit.com.socialmedia.utils.cache;

import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.adapter.out.FollowingPostsRedisAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;


public class FollowingUsersRedisAdapterTest {
    FollowingPostsRedisAdapter sut;

    @BeforeEach
    public void setup() {
        sut = new FollowingPostsRedisAdapter();
    }

    @Test
    void setFollowingPostsReturnVMInRedis_whenGetFollowingPostsReturnVMFromRedis_shouldBeEqual() {
        // Given
        UUID userId = UUID.fromString("b8546f43-3c9f-41d7-9f37-c165aa434549");
        List<FollowingPostsReturnVM> followingPostsReturnVM = List.of(
                new FollowingPostsReturnVM(
                        "1",
                        "1",
                        "Hello it's a post",
                        "2023-10-23 00:00:00"
                ),
                new FollowingPostsReturnVM(
                        "2",
                        "2",
                        "Another Post",
                        "2023-10-23 01:00:00"
                )
        );

        // When
        sut.setFollowingPostsReturnVMInRedis(userId, followingPostsReturnVM);
        List<FollowingPostsReturnVM> followingPostsReturnVMFromRedis = sut.getFollowingPostsReturnVMFromRedis(userId);

        // Then
        Assertions.assertEquals(followingPostsReturnVM, followingPostsReturnVMFromRedis);
    }
}
