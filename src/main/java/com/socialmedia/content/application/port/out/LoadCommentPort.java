package com.socialmedia.content.application.port.out;

import com.socialmedia.content.domain.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadCommentPort {
    public Optional<Comment> loadCommentById(UUID id);
    public List<Comment> loadCommentByUserIdAndPostId(UUID userId, UUID postId);
}
