package com.socialmedia.posts.domain.commands;

import jakarta.validation.constraints.NotNull;

import static com.socialmedia.utils.validation.Validation.validate;

public record CreatePostCommand(
        @NotNull
        String userEmail,
        @NotNull
        String body
) {
    public CreatePostCommand(String userEmail, String body) {
        this.userEmail = userEmail;
        this.body = body;
        validate(this);
    }
}
