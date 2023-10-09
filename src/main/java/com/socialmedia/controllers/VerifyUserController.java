package com.socialmedia.controllers;

import com.socialmedia.services.VerifyUserService;
import com.socialmedia.utils.exceptions.UserAlreadyVerifiedException;
import io.javalin.http.Handler;

import javax.persistence.EntityNotFoundException;

public class VerifyUserController {
    private VerifyUserService service;

    public VerifyUserController(VerifyUserService service) {
        this.service = service;
    }

    public Handler verifyExistingUser = ctx -> {
        try {
            var email = ctx.pathParam("email");

            service.verifyUser(email);

            ctx.status(200).result("User verified successfully.");
        }
        catch (EntityNotFoundException | UserAlreadyVerifiedException e) {
            ctx.status(400).result(e.getMessage());
        }
    };
}
