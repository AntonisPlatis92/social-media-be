package com.socialmedia.views.application.port.in;

import com.socialmedia.posts.domain.Post;
import com.socialmedia.views.adapter.in.vms.CommentReturnVM;
import com.socialmedia.views.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.views.adapter.in.vms.OwnPostsReturnVM;

import java.util.List;
import java.util.UUID;

public interface ViewPostsUseCase {
    List<FollowingPostsReturnVM> viewFollowingPostsInDescendingOrder(UUID userId);
    List<OwnPostsReturnVM> viewOwnPostsLimitedToHundredComments(UUID userId);
    List<CommentReturnVM> viewCommentsOnOwnPosts(UUID userId);
    List<CommentReturnVM> viewCommentsOnOwnAndFollowingPosts(UUID userId);
}
