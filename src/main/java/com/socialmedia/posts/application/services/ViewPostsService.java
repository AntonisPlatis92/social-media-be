package com.socialmedia.posts.application.services;

import com.socialmedia.accounts.application.port.in.LoadFollowsUseCase;
import com.socialmedia.posts.application.memory.FollowingPostsMemory;
import com.socialmedia.posts.application.port.out.LoadCommentsOnOwnAndFollowingPostsPort;
import com.socialmedia.posts.application.port.out.LoadCommentsOnOwnPostsPort;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;
import com.socialmedia.posts.application.port.out.LoadOwnPostsPort;
import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.adapter.in.vms.OwnPostsReturnVM;
import com.socialmedia.posts.application.port.in.ViewPostsUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.socialmedia.posts.application.services.FollowingPostsMemoryService.FOLLOWING_USERS_THRESHOLD;

public class ViewPostsService implements ViewPostsUseCase {

    private final LoadFollowingPostsPort loadFollowingPostsPort;
    private final LoadOwnPostsPort loadOwnPostsPort;
    private final LoadCommentsOnOwnPostsPort loadCommentsOnOwnPostsPort;
    private final LoadCommentsOnOwnAndFollowingPostsPort loadCommentsOnOwnAndFollowingPostsPort;
    private final LoadFollowsUseCase loadFollowsUseCase;
    private final FollowingPostsMemory followingPostsMemory;

    public ViewPostsService(LoadFollowingPostsPort loadFollowingPostsPort,
                            LoadOwnPostsPort loadOwnPostsPort,
                            LoadCommentsOnOwnPostsPort loadCommentsOnOwnPostsPort,
                            LoadCommentsOnOwnAndFollowingPostsPort loadCommentsOnOwnAndFollowingPostsPort,
                            LoadFollowsUseCase loadFollowsUseCase,
                            FollowingPostsMemory followingPostsMemory) {
        this.loadFollowingPostsPort = loadFollowingPostsPort;
        this.loadOwnPostsPort = loadOwnPostsPort;
        this.loadCommentsOnOwnPostsPort = loadCommentsOnOwnPostsPort;
        this.loadCommentsOnOwnAndFollowingPostsPort = loadCommentsOnOwnAndFollowingPostsPort;
        this.loadFollowsUseCase = loadFollowsUseCase;
        this.followingPostsMemory = followingPostsMemory;
    }

    @Override
    public List<FollowingPostsReturnVM> viewFollowingPostsInDescendingOrder(UUID userId) {
        List<FollowingPostsReturnVM> followingPostsReturnVM = new ArrayList<>();
        List<UUID> followingIds = loadFollowsUseCase.loadFollowingUserIds(userId);

        if (followingIds.isEmpty()) {return followingPostsReturnVM;}

        if (followingIds.size() <= FOLLOWING_USERS_THRESHOLD) {
            followingPostsReturnVM.addAll(loadFollowingPostsPort.loadFollowingPostsByFollowingUserIds(followingIds));
        }
        else {
            followingPostsReturnVM.addAll(followingPostsMemory.getPostsForUser(userId));
        }

        return followingPostsReturnVM;
    }

    @Override
    public List<OwnPostsReturnVM> viewOwnPostsLimitedToHundredComments(UUID userId) {

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
