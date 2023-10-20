package com.socialmedia.posts.application.port.out;

import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;

import java.util.List;
import java.util.UUID;

public interface LoadCommentsOnOwnAndFollowingPostsPort {
    List<CommentReturnVM> loadCommentsOnOwnAndFollowingPosts(UUID userId);

}
