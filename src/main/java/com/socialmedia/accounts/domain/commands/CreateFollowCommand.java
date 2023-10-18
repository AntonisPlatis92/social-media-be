package com.socialmedia.accounts.domain.commands;

import com.socialmedia.utils.validation.Validation;
import jakarta.validation.constraints.NotNull;

public record CreateFollowCommand(
        @NotNull
        String followerUserEmail,
        @NotNull
        String followingUserEmail
) {
    public CreateFollowCommand(String followerUserEmail, String followingUserEmail) {
        this.followerUserEmail = followerUserEmail;
        this.followingUserEmail = followingUserEmail;
        Validation.validate(this);
    }
}

