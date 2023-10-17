package com.socialmedia.accounts.domain.commands;

import com.socialmedia.utils.validation.Validation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateFollowCommand(
        @NotNull
        UUID followerId,
        @NotNull
        UUID followingId
) {
    public CreateFollowCommand(UUID followerId, UUID followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
        Validation.validate(this);
    }
}

