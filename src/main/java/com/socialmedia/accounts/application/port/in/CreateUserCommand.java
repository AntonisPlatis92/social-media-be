package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.application.exceptions.PasswordMinimumCharactersException;
import jakarta.validation.constraints.NotNull;

import static com.socialmedia.utils.validation.Validation.validate;

public record CreateUserCommand(
        @NotNull
        String email,
        @NotNull
        String password,
        @NotNull
        long roleId) {
    public static final Long MINIMUM_PASSWORD_CHARACTERS = 8L;

    public CreateUserCommand(String email, String password, long roleId) {
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        validate(this);
        if (this.password.length() < MINIMUM_PASSWORD_CHARACTERS) {
            throw new PasswordMinimumCharactersException(String.format("Password must be at least %s characters long.", MINIMUM_PASSWORD_CHARACTERS));
        }
    }
}
