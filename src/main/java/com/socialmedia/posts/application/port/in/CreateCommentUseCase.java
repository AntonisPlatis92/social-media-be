package com.socialmedia.posts.application.port.in;

import com.socialmedia.posts.domain.commands.CreateCommentCommand;

public interface CreateCommentUseCase {
    public void createComment(CreateCommentCommand command);
}
