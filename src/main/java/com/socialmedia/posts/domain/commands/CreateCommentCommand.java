package com.socialmedia.posts.domain.commands;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static com.socialmedia.utils.validation.Validation.validate;

public record CreateCommentCommand(
        @NotNull
        String userEmail,
        @NotNull
        UUID postId,
        @NotNull
        String body
) {
    public CreateCommentCommand(String userEmail, UUID postId, String body) {
        this.userEmail = userEmail;
        this.postId = postId;
        this.body = body;
        validate(this);
    }
}
