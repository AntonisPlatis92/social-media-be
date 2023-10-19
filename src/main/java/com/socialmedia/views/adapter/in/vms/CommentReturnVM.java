package com.socialmedia.views.adapter.in.vms;

import java.util.UUID;

public record CommentReturnVM(
        String postId,
        UUID userId,
        String commentBody,
        String commentCreationTime
) {
}
