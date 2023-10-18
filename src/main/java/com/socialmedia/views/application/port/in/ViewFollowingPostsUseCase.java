package com.socialmedia.views.application.port.in;

import com.socialmedia.posts.domain.Post;

import java.util.List;

public interface ViewFollowingPostsUseCase {
    List<Post> viewFollowingPostsInDescendingOrder(String userEmail);
}
