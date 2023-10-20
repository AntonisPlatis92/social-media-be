package com.socialmedia.accounts.adapter.in;

import com.socialmedia.accounts.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.accounts.adapter.in.vms.SearchUsersReturnVM;
import com.socialmedia.accounts.application.port.in.LoadFollowsUseCase;
import com.socialmedia.accounts.application.port.in.SearchUsersUseCase;
import com.socialmedia.utils.authentication.JwtUtils;
import io.javalin.http.Handler;

import java.util.UUID;

public class UsersViewController {
    LoadFollowsUseCase loadFollowsUseCase;
    SearchUsersUseCase searchUsersUseCase;

    public UsersViewController(LoadFollowsUseCase loadFollowsUseCase, SearchUsersUseCase searchUsersUseCase) {
        this.loadFollowsUseCase = loadFollowsUseCase;
        this.searchUsersUseCase = searchUsersUseCase;
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

    public Handler searchUsers = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        String searchTerm = ctx.pathParam("term");

        SearchUsersReturnVM searchUsersReturnVM = searchUsersUseCase.searchUsers(searchTerm);
        ctx.status(200).json(searchUsersReturnVM);;
    };
}
