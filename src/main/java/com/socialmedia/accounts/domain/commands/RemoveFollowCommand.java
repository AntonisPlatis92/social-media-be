package com.socialmedia.accounts.domain.commands;

import com.socialmedia.utils.validation.Validation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RemoveFollowCommand(
        @NotNull
        UUID followerUserId,
        @NotNull
        String unfollowingUserEmail
) {
    public RemoveFollowCommand(UUID followerUserId, String unfollowingUserEmail) {
        this.followerUserId = followerUserId;
        this.unfollowingUserEmail = unfollowingUserEmail;
        Validation.validate(this);
    }
}

