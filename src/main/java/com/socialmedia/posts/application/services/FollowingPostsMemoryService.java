package com.socialmedia.posts.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.application.memory.FollowingPostsMemory;
import com.socialmedia.posts.application.port.in.FollowingPostsMemoryUseCase;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;

import java.util.List;
import java.util.UUID;

public class FollowingPostsMemoryService implements FollowingPostsMemoryUseCase {
    LoadFollowingPostsPort loadFollowingPostsPort;
    FollowingPostsMemory followingPostsMemory;
    LoadUserUseCase loadUserUseCase;

    public static final Integer FOLLOWING_USERS_THRESHOLD = 1000;

    public FollowingPostsMemoryService(LoadFollowingPostsPort loadFollowingPostsPort, FollowingPostsMemory followingPostsMemory, LoadUserUseCase loadUserUseCase) {
        this.loadFollowingPostsPort = loadFollowingPostsPort;
        this.followingPostsMemory = followingPostsMemory;
        this.loadUserUseCase = loadUserUseCase;
    }

    @Override
    public void addUserInFollowingPostsMemoryIfNeeded(User user) {
        if (user.getFollowing().size() < FOLLOWING_USERS_THRESHOLD) {return;}

        List<UUID> followingUserIds = user.getFollowing().stream()
                .map(Follow::getFollowingId)
                .toList();

        List<FollowingPostsReturnVM> followingUserIdsPosts = loadFollowingPostsPort.loadFollowingPostsByFollowingUserIds(followingUserIds);

        followingPostsMemory.addPostForUser(user.getUserId(), followingUserIdsPosts);
    }

    @Override
    public void addUsersInFollowingPostsMemoryOnStartup() {
        List<User> usersWithManyFollowing = loadUserUseCase.loadUsersByFollowingMoreThan(FOLLOWING_USERS_THRESHOLD);
        usersWithManyFollowing.forEach(this::addUserInFollowingPostsMemoryIfNeeded);
    }

    @Override
    public void addPostForUserInFollowingPostsMemoryIfNeeded(User user, FollowingPostsReturnVM followingPostReturnVM) {
        if (user.getFollowing().size() < FOLLOWING_USERS_THRESHOLD) {return;}

        followingPostsMemory.addPostForUser(user.getUserId(), List.of(followingPostReturnVM));
    }
}
