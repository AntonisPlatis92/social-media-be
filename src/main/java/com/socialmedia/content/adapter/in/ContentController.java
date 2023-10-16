package com.socialmedia.content.adapter.in;

import com.socialmedia.content.adapter.in.vms.CreateCommentVM;
import com.socialmedia.content.adapter.in.vms.CreatePostVM;
import com.socialmedia.content.application.port.in.CreateCommentUseCase;
import com.socialmedia.content.application.port.in.CreatePostUseCase;
import com.socialmedia.content.domain.commands.CreateCommentCommand;
import com.socialmedia.content.domain.commands.CreatePostCommand;
import com.socialmedia.utils.authentication.JwtUtils;
import io.javalin.http.Handler;

import java.util.UUID;

public class ContentController {
    private CreatePostUseCase createPostUseCase;
    private CreateCommentUseCase createCommentUseCase;


    public ContentController(CreatePostUseCase createPostUseCase, CreateCommentUseCase createCommentUseCase) {
        this.createPostUseCase = createPostUseCase;
        this.createCommentUseCase = createCommentUseCase;
    }

    public Handler createNewPost = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        CreatePostVM createPostVM = ctx.bodyAsClass(CreatePostVM.class);
        if (createPostVM == null) {
            ctx.status(400).result("Invalid request body");
        }

        CreatePostCommand command = new CreatePostCommand(
                userId,
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
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        CreateCommentVM createCommentVM = ctx.bodyAsClass(CreateCommentVM.class);
        if (createCommentVM == null) {
            ctx.status(400).result("Invalid request body");
        }

        CreateCommentCommand command = new CreateCommentCommand(
                userId,
                createCommentVM.postId(),
                createCommentVM.body()
        );

        createCommentUseCase.createComment(command);

        ctx.status(201).result("Comment created successfully.");
    };

}
