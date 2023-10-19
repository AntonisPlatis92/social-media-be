package com.socialmedia.views.adapter.in.vms;

import java.util.List;

public record OwnPostsReturnVM(
        String postId,
        String postBody,
        String postCreationTime,
        List<CommentReturnVM> comments
) {
}
