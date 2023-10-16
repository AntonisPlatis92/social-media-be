package com.socialmedia.content.application.port.out;

import com.socialmedia.content.domain.Post;

public interface CreatePostPort {
    void createNewPost(Post post);
}
