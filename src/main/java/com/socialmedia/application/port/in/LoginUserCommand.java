package com.socialmedia.application.port.in;

import jakarta.validation.constraints.NotNull;

import static com.socialmedia.common.validation.Validation.validate;

public record LoginUserCommand(
        @NotNull
        String email,
        @NotNull
        String password
) {
    public LoginUserCommand(String email, String password) {
        this.email = email;
        this.password = password;
        validate(this);
    }
}
