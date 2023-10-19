package com.socialmedia.posts.application.port.out;

import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;

import java.util.List;
import java.util.UUID;

public interface LoadFollowingPostsPort {
    List<FollowingPostsReturnVM> loadFollowingPosts(UUID userId);
}
