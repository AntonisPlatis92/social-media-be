package com.socialmedia.posts.adapter.in.vms;

import com.socialmedia.posts.domain.Post;

import static com.socialmedia.utils.formatters.DateFormatter.FORMATTER;

public record FollowingPostsReturnVM(
        String postId,
        String postUserId,
        String postBody,
        String postCreationTime
) {
    public static FollowingPostsReturnVM createFollowingPostsReturnVmFromPost(Post post) {
        return new FollowingPostsReturnVM(
                post.getId().toString(),
                post.getUserId().toString(),
                post.getBody(),
                FORMATTER.format(post.getCreationTime())
        );
    }
}
