package com.socialmedia.posts.application.services;

import com.socialmedia.posts.application.port.out.LoadCommentsOnOwnAndFollowingPostsPort;
import com.socialmedia.posts.application.port.out.LoadCommentsOnOwnPostsPort;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;
import com.socialmedia.posts.application.port.out.LoadOwnPostsPort;
import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.adapter.in.vms.OwnPostsReturnVM;
import com.socialmedia.posts.application.port.in.ViewPostsUseCase;

import java.util.List;
import java.util.UUID;

public class ViewPostsService implements ViewPostsUseCase {

    private final LoadFollowingPostsPort loadFollowingPostsPort;
    private final LoadOwnPostsPort loadOwnPostsPort;
    private final LoadCommentsOnOwnPostsPort loadCommentsOnOwnPostsPort;
    private final LoadCommentsOnOwnAndFollowingPostsPort loadCommentsOnOwnAndFollowingPostsPort;

    public ViewPostsService(LoadFollowingPostsPort loadFollowingPostsPort,
                            LoadOwnPostsPort loadOwnPostsPort,
                            LoadCommentsOnOwnPostsPort loadCommentsOnOwnPostsPort,
                            LoadCommentsOnOwnAndFollowingPostsPort loadCommentsOnOwnAndFollowingPostsPort) {
        this.loadFollowingPostsPort = loadFollowingPostsPort;
        this.loadOwnPostsPort = loadOwnPostsPort;
        this.loadCommentsOnOwnPostsPort = loadCommentsOnOwnPostsPort;
        this.loadCommentsOnOwnAndFollowingPostsPort = loadCommentsOnOwnAndFollowingPostsPort;
    }

    @Override
    public List<FollowingPostsReturnVM> viewFollowingPostsInDescendingOrder(UUID userId) {

        return loadFollowingPostsPort.loadFollowingPosts(userId);
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
