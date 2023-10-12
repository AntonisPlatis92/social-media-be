package com.socialmedia.utils.authentication.exceptions;

import com.socialmedia.accounts.domain.exceptions.*;
import io.javalin.Javalin;
import jakarta.validation.ConstraintViolationException;

public class ExceptionHandler {
    public static void setupExceptionHandler(Javalin app) {
        app.exception(PasswordMinimumCharactersException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(UserAlreadyCreatedException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(ConstraintViolationException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(UserAlreadyVerifiedException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(UserNotFoundException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(LoginFailedException.class, (e, ctx) -> {
            ctx.status(401);
            ctx.result(e.getMessage());
        });
    }
}
