package com.socialmedia.adapter.in.web;

import com.socialmedia.application.domain.services.VerifyUserService;
import com.socialmedia.application.domain.utils.exceptions.UserAlreadyVerifiedException;
import com.socialmedia.application.domain.utils.exceptions.UserNotFoundException;
import com.socialmedia.application.port.in.VerifyUserCommand;
import io.javalin.http.Handler;
import jakarta.validation.ConstraintViolationException;

public class VerifyUserController {
    private VerifyUserService service;

    public VerifyUserController(VerifyUserService service) {
        this.service = service;
    }

    public Handler verifyExistingUser = ctx -> {
        try {
            String email = ctx.pathParam("email");

            VerifyUserCommand command = new VerifyUserCommand(email);
            service.verifyUser(command);

            ctx.status(200).result("User verified successfully.");
        }
        catch (UserNotFoundException | UserAlreadyVerifiedException |
               ConstraintViolationException e) {
            ctx.status(400).result(e.getMessage());
        }
    };
}
