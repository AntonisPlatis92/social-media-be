package com.socialmedia.posts.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.posts.application.port.in.FollowingPostsCacheUseCase;
import com.socialmedia.posts.application.port.out.LoadCommentsOnOwnAndFollowingPostsPort;
import com.socialmedia.posts.application.port.out.LoadCommentsOnOwnPostsPort;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;
import com.socialmedia.posts.application.port.out.LoadOwnPostsPort;
import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.adapter.in.vms.OwnPostReturnVM;
import com.socialmedia.posts.application.port.in.ViewPostsUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.socialmedia.posts.application.services.FollowingPostsCacheService.FOLLOWING_USERS_THRESHOLD;

public class ViewPostsService implements ViewPostsUseCase {

    private final LoadFollowingPostsPort loadFollowingPostsPort;
    private final LoadOwnPostsPort loadOwnPostsPort;
    private final LoadCommentsOnOwnPostsPort loadCommentsOnOwnPostsPort;
    private final LoadCommentsOnOwnAndFollowingPostsPort loadCommentsOnOwnAndFollowingPostsPort;
    private final LoadUserUseCase loadUserUseCase;
    private final FollowingPostsCacheUseCase followingPostsCacheUseCase;

    public ViewPostsService(LoadFollowingPostsPort loadFollowingPostsPort,
                            LoadOwnPostsPort loadOwnPostsPort,
                            LoadCommentsOnOwnPostsPort loadCommentsOnOwnPostsPort,
                            LoadCommentsOnOwnAndFollowingPostsPort loadCommentsOnOwnAndFollowingPostsPort,
                            LoadUserUseCase loadUserUseCase,
                            FollowingPostsCacheUseCase followingPostsCacheUseCase) {
        this.loadFollowingPostsPort = loadFollowingPostsPort;
        this.loadOwnPostsPort = loadOwnPostsPort;
        this.loadCommentsOnOwnPostsPort = loadCommentsOnOwnPostsPort;
        this.loadCommentsOnOwnAndFollowingPostsPort = loadCommentsOnOwnAndFollowingPostsPort;
        this.loadUserUseCase = loadUserUseCase;
        this.followingPostsCacheUseCase = followingPostsCacheUseCase;
    }

    @Override
    public List<FollowingPostsReturnVM> viewFollowingPostsInDescendingOrder(UUID userId) {
        List<FollowingPostsReturnVM> followingPostsReturnVM = new ArrayList<>();

        User user = loadUserUseCase.loadUserById(userId).orElseThrow(() -> new UserNotFoundException("User doesn't exist."));
        List<UUID> followingIds = user.getFollowing().stream().map(Follow::getFollowingId).toList();

        if (followingIds.isEmpty()) {return followingPostsReturnVM;}

        if (followingIds.size() <= FOLLOWING_USERS_THRESHOLD) {
            followingPostsReturnVM.addAll(loadFollowingPostsPort.loadFollowingPostsByFollowingUserIds(followingIds));
        }
        else {
            followingPostsReturnVM.addAll(followingPostsCacheUseCase.getUserFollowingPostsFromCache(user));
        }

        return followingPostsReturnVM;
    }

    @Override
    public List<OwnPostReturnVM> viewOwnPostsLimitedToHundredComments(UUID userId) {

        return loadOwnPostsPort.loadOwnPosts(userId);
    }

    @Override
    public List<CommentReturnVM> viewCommentsOnOwnPosts(UUID userId) {

        return loadCommentsOnOwnPostsPort.loadCommentsOnOwnPosts(userId);
    }

    @Override
    public List<CommentReturnVM> viewCommentsOnOwnAndFollowingPosts(UUID userId) {

        return loadCommentsOnOwnAndFollowingPostsPort.loadCommentsOnOwnAndFollowingPosts(userId);
    }
}
