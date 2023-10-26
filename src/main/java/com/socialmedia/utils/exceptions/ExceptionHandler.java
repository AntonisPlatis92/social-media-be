package com.socialmedia.utils.exceptions;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.socialmedia.accounts.domain.exceptions.*;
import com.socialmedia.posts.domain.exceptions.CommentsLimitException;
import com.socialmedia.posts.domain.exceptions.PostCharsLimitException;
import com.socialmedia.posts.domain.exceptions.PostNotFoundException;
import io.javalin.Javalin;
import jakarta.validation.ConstraintViolationException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;

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
        app.exception(UnrecognizedPropertyException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(SignatureException.class, (e, ctx) -> {
            ctx.status(401);
            ctx.result(e.getMessage());
        });
        app.exception(PostCharsLimitException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(RoleNotFoundException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(PostNotFoundException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(CommentsLimitException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(ExpiredJwtException.class, (e, ctx) -> {
            ctx.status(401);
            ctx.result(e.getMessage());
        });
        app.exception(FollowAlreadyExistsException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(FollowNotFoundException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });
        app.exception(RuntimeException.class, (e, ctx) -> {
            ctx.status(500);
            ctx.result(e.getMessage());
        });
    }
}
