package com.socialmedia.content.application.port.out;

import com.socialmedia.content.domain.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadPostPort {
    Optional<Post> loadPostById(UUID id);
    List<Post> loadPostsByUserEmail(String userEmail);
}
