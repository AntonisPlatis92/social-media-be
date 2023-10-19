package com.socialmedia.posts.adapter.in.vms;

import java.util.List;

public record OwnPostsReturnVM(
        String postId,
        String postBody,
        String postCreationTime,
        List<CommentReturnVM> comments
) {
}
