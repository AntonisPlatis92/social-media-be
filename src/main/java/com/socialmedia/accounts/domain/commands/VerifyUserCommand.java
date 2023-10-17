package com.socialmedia.accounts.domain.commands;

import jakarta.validation.constraints.NotNull;

import static com.socialmedia.utils.validation.Validation.validate;

public record VerifyUserCommand(@NotNull String email) {
    public VerifyUserCommand {
        validate(this);
    }
}
