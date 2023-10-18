package com.socialmedia.accounts.domain.commands;

import com.socialmedia.utils.validation.Validation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RemoveFollowCommand(
        @NotNull
        UUID followerId,
        @NotNull
        UUID followingId
) {
    public RemoveFollowCommand(UUID followerId, UUID followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
        Validation.validate(this);
    }
}

