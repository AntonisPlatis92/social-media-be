package com.socialmedia.posts.application.port.out;

import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;

import java.util.List;
import java.util.UUID;

public interface LoadCommentsOnOwnPostsPort {
    List<CommentReturnVM> loadCommentsOnOwnPosts(UUID userId);
}
