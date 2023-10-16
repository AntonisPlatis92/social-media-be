package com.socialmedia.content.application.port.out;

import com.socialmedia.content.domain.Comment;

public interface CreateCommentPort {
    void createNewComment(Comment comment);
}
