package com.socialmedia.posts.application.port.in;

import com.socialmedia.posts.domain.commands.CreatePostCommand;

public interface CreatePostUseCase {
    void createPost(CreatePostCommand command);
}
