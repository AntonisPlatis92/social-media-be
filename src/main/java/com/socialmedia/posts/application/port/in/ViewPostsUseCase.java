package com.socialmedia.posts.application.port.in;

import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.adapter.in.vms.OwnPostReturnVM;

import java.util.List;
import java.util.UUID;

public interface ViewPostsUseCase {
    List<FollowingPostsReturnVM> viewFollowingPostsInDescendingOrder(UUID userId);
    List<OwnPostReturnVM> viewOwnPostsLimitedToHundredComments(UUID userId);
    List<CommentReturnVM> viewCommentsOnOwnPosts(UUID userId);
    List<CommentReturnVM> viewCommentsOnOwnAndFollowingPosts(UUID userId);
}
