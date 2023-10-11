package com.socialmedia.accounts.adapter.in.web;

import com.socialmedia.accounts.adapter.in.web.vms.LoginUserVM;
import com.socialmedia.accounts.application.services.LoginUserService;
import com.socialmedia.accounts.application.exceptions.UserNotFoundException;
import com.socialmedia.accounts.application.port.in.LoginUserCommand;
import io.javalin.http.Handler;
import jakarta.validation.ConstraintViolationException;

public class LoginUserController {
    private LoginUserService service;

    public LoginUserController(LoginUserService service) {
        this.service = service;
    }

    public Handler loginExistingUser = ctx -> {
        try {
            LoginUserVM loginUserVM = ctx.bodyAsClass(LoginUserVM.class);
            if (loginUserVM == null) {
                ctx.status(400).result("Invalid request body");
            }

            LoginUserCommand command = new LoginUserCommand(
                    loginUserVM.email(),
                    loginUserVM.password()
            );
            String loginToken = service.loginUser(command);

            if (loginToken != null) {
                ctx.status(200).result(loginToken);
            } else {
                ctx.status(401).result("User login failed.");
            }
        }
        catch (UserNotFoundException | ConstraintViolationException e) {
            ctx.status(400).result(e.getMessage());
        }


    };
}