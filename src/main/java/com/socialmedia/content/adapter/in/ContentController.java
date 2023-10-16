package com.socialmedia.content.adapter.in;

import com.socialmedia.content.adapter.in.vms.CreatePostVM;
import com.socialmedia.content.application.port.in.CreatePostUseCase;
import com.socialmedia.content.domain.commands.CreatePostCommand;
import com.socialmedia.utils.authentication.JwtUtils;
import io.javalin.http.Handler;

import java.util.UUID;

public class ContentController {
    private CreatePostUseCase createPostUseCase;


    public ContentController(CreatePostUseCase createPostUseCase) {
        this.createPostUseCase = createPostUseCase;
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
                createPostVM.postBody()
        );

        createPostUseCase.createPost(command);

        ctx.status(201).result("Post created successfully.");
    };

}
