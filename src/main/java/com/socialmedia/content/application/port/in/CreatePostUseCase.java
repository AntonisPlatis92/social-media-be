package com.socialmedia.content.application.port.in;

import com.socialmedia.content.domain.commands.CreatePostCommand;

public interface CreatePostUseCase {
    public void createPost(CreatePostCommand command);
}
