package com.socialmedia.posts.application.port.out;

import com.socialmedia.posts.domain.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadPostPort {
    Optional<Post> loadPostById(UUID id);
    List<Post> loadPostsByUserId(UUID userId);
}
