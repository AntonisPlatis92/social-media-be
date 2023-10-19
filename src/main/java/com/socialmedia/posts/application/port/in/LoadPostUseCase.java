package com.socialmedia.posts.application.port.in;

import com.socialmedia.posts.domain.Post;

import java.util.List;
import java.util.UUID;

public interface LoadPostUseCase {
    List<Post> loadPostsByUserId(UUID userId);
}
