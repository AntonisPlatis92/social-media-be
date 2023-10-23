package com.socialmedia.posts.application.port.in;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;

public interface FollowingPostsMemoryUseCase {
    void addUserInFollowingPostsMemoryIfNeeded(User user);
    void addUsersInFollowingPostsMemoryOnStartup();
    void addPostForUserInFollowingPostsMemoryIfNeeded(User user, FollowingPostsReturnVM followingPostsReturnVM);
}
