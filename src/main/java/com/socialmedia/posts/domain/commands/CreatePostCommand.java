package com.socialmedia.posts.domain.commands;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static com.socialmedia.utils.validation.Validation.validate;

public record CreatePostCommand(
        @NotNull
        UUID userId,
        @NotNull
        String body
) {
    public CreatePostCommand(UUID userId, String body) {
        this.userId = userId;
        this.body = body;
        validate(this);
    }
}
