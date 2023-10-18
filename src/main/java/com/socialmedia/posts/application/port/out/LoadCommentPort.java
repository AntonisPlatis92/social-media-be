package com.socialmedia.posts.application.port.out;

import com.socialmedia.posts.domain.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadCommentPort {
    Optional<Comment> loadCommentById(UUID id);
    List<Comment> loadCommentByUserEmailAndPostId(String userEmail, UUID postId);
}
