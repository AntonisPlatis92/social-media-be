package com.socialmedia.posts.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.application.port.in.FollowingPostsCacheUseCase;
import com.socialmedia.posts.application.port.out.FollowingPostsCachePort;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;

import java.util.List;
import java.util.UUID;

public class FollowingPostsCacheService implements FollowingPostsCacheUseCase {
    LoadFollowingPostsPort loadFollowingPostsPort;
    FollowingPostsCachePort followingUsersRedisPort;
    LoadUserUseCase loadUserUseCase;

    public static final Integer FOLLOWING_USERS_THRESHOLD = 2;

    public FollowingPostsCacheService(LoadFollowingPostsPort loadFollowingPostsPort, FollowingPostsCachePort followingUsersRedisPort, LoadUserUseCase loadUserUseCase) {
        this.loadFollowingPostsPort = loadFollowingPostsPort;
        this.followingUsersRedisPort = followingUsersRedisPort;
        this.loadUserUseCase = loadUserUseCase;
    }

    @Override
    public void addUserInFollowingPostsCache(User user) {
        List<UUID> followingUserIds = user.getFollowing().stream()
                .map(Follow::getFollowingId)
                .toList();

        List<FollowingPostsReturnVM> followingUserIdsPosts = loadFollowingPostsPort.loadFollowingPostsByFollowingUserIds(followingUserIds);

        followingUsersRedisPort.setFollowingPostsReturnVMInRedis(user.getUserId(), followingUserIdsPosts);
    }


    @Override
    public void addPostForUserInFollowingPostsMemory(User user, FollowingPostsReturnVM followingPostReturnVM) {
        if (user.getFollowing().size() < FOLLOWING_USERS_THRESHOLD) {return;}

        List<FollowingPostsReturnVM> followingPosts = followingUsersRedisPort.getFollowingPostsReturnVMFromRedis(user.getUserId());

        if (followingPosts == null) {
            addUserInFollowingPostsCache(user);
            followingPosts = followingUsersRedisPort.getFollowingPostsReturnVMFromRedis(user.getUserId());
        }

        followingPosts.add(0, followingPostReturnVM);
        followingUsersRedisPort.setFollowingPostsReturnVMInRedis(user.getUserId(), followingPosts);
    }

    @Override
    public List<FollowingPostsReturnVM> getUserFollowingPostsFromCache(User user) {
        List<FollowingPostsReturnVM> followingPosts = followingUsersRedisPort.getFollowingPostsReturnVMFromRedis(user.getUserId());

        if (followingPosts == null) {
            addUserInFollowingPostsCache(user);
            followingPosts = followingUsersRedisPort.getFollowingPostsReturnVMFromRedis(user.getUserId());
        }

        return followingPosts;
    }
}
