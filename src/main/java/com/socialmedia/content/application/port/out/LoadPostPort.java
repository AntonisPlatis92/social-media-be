package com.socialmedia.content.application.port.out;

import com.socialmedia.content.domain.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadPostPort {
    public Optional<Post> loadPostById(UUID id);
    public List<Post> loadPostByUserId(UUID userId);
}
