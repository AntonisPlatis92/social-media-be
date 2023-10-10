package com.socialmedia.application.domain.controllers;

import com.socialmedia.application.domain.services.VerifyUserService;
import com.socialmedia.application.domain.utils.exceptions.UserAlreadyVerifiedException;
import com.socialmedia.application.domain.utils.exceptions.UserNotFoundException;
import io.javalin.http.Handler;

public class VerifyUserController {
    private VerifyUserService service;

    public VerifyUserController(VerifyUserService service) {
        this.service = service;
    }

    public Handler verifyExistingUser = ctx -> {
        try {
            String email = ctx.pathParam("email");

            service.verifyUser(email);

            ctx.status(200).result("User verified successfully.");
        }
        catch (UserNotFoundException | UserAlreadyVerifiedException e) {
            ctx.status(400).result(e.getMessage());
        }
    };
}
