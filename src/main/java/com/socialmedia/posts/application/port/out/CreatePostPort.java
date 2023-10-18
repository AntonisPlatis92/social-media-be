package com.socialmedia.posts.application.port.out;

import com.socialmedia.posts.domain.Post;

public interface CreatePostPort {
    void createNewPost(Post post);
}
