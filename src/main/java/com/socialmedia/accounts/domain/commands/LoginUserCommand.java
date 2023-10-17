package com.socialmedia.accounts.domain.commands;

import jakarta.validation.constraints.NotNull;

import static com.socialmedia.utils.validation.Validation.validate;

public record LoginUserCommand(
        @NotNull
        String email,
        @NotNull
        String password
) {
    public LoginUserCommand {
        validate(this);
    }
}
