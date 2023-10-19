package com.socialmedia.views.adapter.in;

import com.socialmedia.utils.authentication.JwtUtils;
import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.views.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.posts.adapter.in.vms.OwnPostsReturnVM;
import com.socialmedia.views.application.port.in.ViewFollowsUseCase;
import com.socialmedia.posts.application.port.in.ViewPostsUseCase;
import io.javalin.http.Handler;

import java.util.List;
import java.util.UUID;

public class ViewController {
    ViewPostsUseCase viewPostsUseCase;
    ViewFollowsUseCase viewFollowsUseCase;

    public ViewController(ViewPostsUseCase viewPostsUseCase, ViewFollowsUseCase viewFollowsUseCase) {
        this.viewPostsUseCase = viewPostsUseCase;
        this.viewFollowsUseCase = viewFollowsUseCase;
    }

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

        List<CommentReturnVM> commentsOnOwnPostsVM = viewPostsUseCase.viewCommentsOnOwnPosts(userId);

        ctx.status(200).json(commentsOnOwnPostsVM);;
    };

    public Handler viewCommentsOnOwnAndFollowingPosts = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        List<CommentReturnVM> commentsOnOwnAndFollowingPosts = viewPostsUseCase.viewCommentsOnOwnAndFollowingPosts(userId);

        ctx.status(200).json(commentsOnOwnAndFollowingPosts);;
    };

    public Handler viewOwnFollows = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        FollowsReturnVM followsReturnVM = viewFollowsUseCase.viewOwnFollowersAndFollowing(userId);

        ctx.status(200).json(followsReturnVM);;
    };
}
