package com.socialmedia.content.domain.commands;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static com.socialmedia.utils.validation.Validation.validate;

public record CreateCommentCommand(
        @NotNull
        UUID userId,
        @NotNull
        UUID postId,
        @NotNull
        String body
) {
    public CreateCommentCommand {
        validate(this);
    }
}
