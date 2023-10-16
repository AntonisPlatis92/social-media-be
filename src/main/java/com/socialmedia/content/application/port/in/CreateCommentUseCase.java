package com.socialmedia.content.application.port.in;

import com.socialmedia.content.domain.commands.CreateCommentCommand;

public interface CreateCommentUseCase {
    public void createComment(CreateCommentCommand command);
}
