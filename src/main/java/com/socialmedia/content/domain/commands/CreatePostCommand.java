package com.socialmedia.content.domain.commands;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static com.socialmedia.utils.validation.Validation.validate;

public record CreatePostCommand(
        @NotNull
        UUID userId,
        @NotNull
        String body
) {
    public CreatePostCommand {
        validate(this);
    }
}
