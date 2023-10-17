package com.socialmedia.follows.domain.commands;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static com.socialmedia.utils.validation.Validation.validate;

public record CreateFollowCommand(
        @NotNull
        UUID followerId,
        @NotNull
        UUID followingId
) {
    public CreateFollowCommand(UUID followerId, UUID followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
        validate(this);
    }
}

