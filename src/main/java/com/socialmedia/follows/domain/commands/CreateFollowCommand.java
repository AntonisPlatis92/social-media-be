package com.socialmedia.follows.domain.commands;

import com.socialmedia.utils.validation.Validation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateFollowCommand(
        @NotNull
        UUID followerId,
        @NotNull
        UUID followingId
) {
    public CreateFollowCommand {
        Validation.validate(this);
    }
}

