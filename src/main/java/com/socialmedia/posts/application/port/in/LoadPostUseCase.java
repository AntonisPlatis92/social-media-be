package com.socialmedia.posts.application.port.in;

import com.socialmedia.posts.domain.Post;

import java.util.List;

public interface LoadPostUseCase {
    List<Post> loadPostsByUserEmail(String userEmail);
}
