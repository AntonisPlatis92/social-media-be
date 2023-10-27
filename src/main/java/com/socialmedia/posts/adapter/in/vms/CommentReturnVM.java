package com.socialmedia.posts.adapter.in.vms;

import com.socialmedia.utils.formatters.DateFormatter;

import java.time.Instant;
import java.util.UUID;

public record CommentReturnVM(
        String postId,
        String commentUserEmail,
        String commentBody,
        String commentCreationTime
) {
    public CommentReturnVM(UUID postId, String commentUserEmail, String commentBody, Instant commentCreationTime) {
        this(postId.toString(), commentUserEmail, commentBody, DateFormatter.FORMATTER.format(commentCreationTime));
    }
}
