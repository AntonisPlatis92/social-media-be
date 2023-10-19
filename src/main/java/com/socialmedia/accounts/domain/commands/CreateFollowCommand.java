package com.socialmedia.accounts.domain.commands;

import com.socialmedia.utils.validation.Validation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateFollowCommand(
        @NotNull
        UUID followerUserId,
        @NotNull
        String followingUserEmail
) {
    public CreateFollowCommand(UUID followerUserId, String followingUserEmail) {
        this.followerUserId = followerUserId;
        this.followingUserEmail = followingUserEmail;
        Validation.validate(this);
    }
}

