package com.socialmedia.views.adapter.in;

import com.socialmedia.posts.domain.Post;
import com.socialmedia.utils.authentication.JwtUtils;
import com.socialmedia.views.application.port.in.ViewFollowingPostsUseCase;
import io.javalin.http.Handler;

import java.util.List;

public class ViewController {
    ViewFollowingPostsUseCase viewFollowingPostsUseCase;

    public ViewController(ViewFollowingPostsUseCase viewFollowingPostsUseCase) {
        this.viewFollowingPostsUseCase = viewFollowingPostsUseCase;
    }

    public Handler viewFollowingPosts = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        String userEmail = JwtUtils.extractUserEmailFromToken(authorizationToken);

        List<Post> posts = viewFollowingPostsUseCase.viewFollowingPostsInDescendingOrder(userEmail);

        ctx.status(200).json(posts);;
    };
}
