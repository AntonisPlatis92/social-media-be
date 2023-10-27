package com.socialmedia.posts.adapter.in.vms;

import java.util.List;

public record OwnPostReturnVM(
        String postId,
        String postBody,
        String postCreationTime,
        List<CommentReturnVM> comments
) {
}
