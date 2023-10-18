package com.socialmedia.posts.adapter.in;

import com.socialmedia.posts.adapter.in.vms.CreateCommentVM;
import com.socialmedia.posts.adapter.in.vms.CreatePostVM;
import com.socialmedia.posts.application.port.in.CreateCommentUseCase;
import com.socialmedia.posts.application.port.in.CreatePostUseCase;
import com.socialmedia.posts.domain.commands.CreateCommentCommand;
import com.socialmedia.posts.domain.commands.CreatePostCommand;
import com.socialmedia.utils.authentication.JwtUtils;
import io.javalin.http.Handler;

public class PostController {
    private CreatePostUseCase createPostUseCase;
    private CreateCommentUseCase createCommentUseCase;


    public PostController(CreatePostUseCase createPostUseCase, CreateCommentUseCase createCommentUseCase) {
        this.createPostUseCase = createPostUseCase;
        this.createCommentUseCase = createCommentUseCase;
    }

    public Handler createNewPost = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        String userEmail = JwtUtils.extractUserEmailFromToken(authorizationToken);

        CreatePostVM createPostVM = ctx.bodyAsClass(CreatePostVM.class);
        if (createPostVM == null) {
            ctx.status(400).result("Invalid request body");
        }

        CreatePostCommand command = new CreatePostCommand(
                userEmail,
                createPostVM.body()
        );

        createPostUseCase.createPost(command);

        ctx.status(201).result("Post created successfully.");
    };

    public Handler createNewComment = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        String userEmail = JwtUtils.extractUserEmailFromToken(authorizationToken);

        CreateCommentVM createCommentVM = ctx.bodyAsClass(CreateCommentVM.class);
        if (createCommentVM == null) {
            ctx.status(400).result("Invalid request body");
        }

        CreateCommentCommand command = new CreateCommentCommand(
                userEmail,
                createCommentVM.postId(),
                createCommentVM.body()
        );

        createCommentUseCase.createComment(command);

        ctx.status(201).result("Comment created successfully.");
    };

}
