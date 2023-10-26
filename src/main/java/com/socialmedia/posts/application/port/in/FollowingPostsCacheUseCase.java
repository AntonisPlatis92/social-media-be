package com.socialmedia.posts.application.port.in;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;

import java.util.List;

public interface FollowingPostsCacheUseCase {
    void addUserInFollowingPostsCache(User user);
    void addPostForUserInFollowingPostsMemory(User user, FollowingPostsReturnVM followingPostsReturnVM);
    List<FollowingPostsReturnVM> getUserFollowingPostsFromCache(User user);
}
