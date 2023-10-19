package com.socialmedia.posts.adapter.in.vms;

public record CommentReturnVM(
        String postId,
        String commentUserEmail,
        String commentBody,
        String commentCreationTime
) {
}
