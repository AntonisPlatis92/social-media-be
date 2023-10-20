package com.socialmedia.accounts.adapter.in;

import com.socialmedia.accounts.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.accounts.application.port.in.LoadFollowsUseCase;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.utils.authentication.JwtUtils;
import io.javalin.http.Handler;

import java.util.List;
import java.util.UUID;

public class FollowsViewController {
    LoadFollowsUseCase loadFollowsUseCase;

    public FollowsViewController(LoadFollowsUseCase loadFollowsUseCase) {
        this.loadFollowsUseCase = loadFollowsUseCase;
    }

    public Handler viewOwnFollows = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        FollowsReturnVM followsReturnVM = loadFollowsUseCase.loadFollowsByUserId(userId);

        ctx.status(200).json(followsReturnVM);;
    };
}
