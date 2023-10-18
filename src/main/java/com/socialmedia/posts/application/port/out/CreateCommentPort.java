package com.socialmedia.posts.application.port.out;

import com.socialmedia.posts.domain.Comment;

public interface CreateCommentPort {
    void createNewComment(Comment comment);
}
