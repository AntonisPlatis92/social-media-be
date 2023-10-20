package com.socialmedia.posts.adapter.in;

import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.adapter.in.vms.OwnPostsReturnVM;
import com.socialmedia.posts.application.port.in.ViewPostsUseCase;
import com.socialmedia.utils.authentication.JwtUtils;
import io.javalin.http.Handler;

import java.util.List;
import java.util.UUID;

public class PostViewController {
    ViewPostsUseCase viewPostsUseCase;

    public PostViewController(ViewPostsUseCase viewPostsUseCase) {
        this.viewPostsUseCase = viewPostsUseCase;
    }

    public Handler viewFollowingPosts = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        List<FollowingPostsReturnVM> followingPostsReturnVM = viewPostsUseCase.viewFollowingPostsInDescendingOrder(userId);

        ctx.status(200).json(followingPostsReturnVM);;
    };

    public Handler viewOwnPosts = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        List<OwnPostsReturnVM> ownPostsReturnVM = viewPostsUseCase.viewOwnPostsLimitedToHundredComments(userId);

        ctx.status(200).json(ownPostsReturnVM);;
    };

    public Handler viewCommentsOnOwnPosts = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        List<CommentReturnVM> ownPostsReturnVM = viewPostsUseCase.viewCommentsOnOwnPosts(userId);

        ctx.status(200).json(ownPostsReturnVM);;
    };

    public Handler viewCommentsOnOwnAndFollowingPosts = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        List<CommentReturnVM> ownPostsReturnVM = viewPostsUseCase.viewCommentsOnOwnAndFollowingPosts(userId);

        ctx.status(200).json(ownPostsReturnVM);;
    };
}
