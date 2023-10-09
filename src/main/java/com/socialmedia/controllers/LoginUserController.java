package com.socialmedia.controllers;

import com.socialmedia.controllers.vms.LoginUserVM;
import com.socialmedia.services.LoginUserService;
import io.javalin.http.Handler;

import javax.persistence.EntityNotFoundException;

public class LoginUserController {
    private LoginUserService service;

    public LoginUserController(LoginUserService service) {
        this.service = service;
    }

    public Handler loginExistingUser = ctx -> {
        try {
            var loginUserVM = ctx.bodyAsClass(LoginUserVM.class);
            if (loginUserVM == null) {
                ctx.status(400).result("Invalid request body");
            }

            boolean userLoginSuccessful = service.loginUser(
                    loginUserVM.email(),
                    loginUserVM.password()
            );

            if (userLoginSuccessful) {
                ctx.status(201).result("User login successful.");
            } else {
                ctx.status(400).result("User login failed.");
            }
        }
        catch (EntityNotFoundException e) {
            ctx.status(400).result(e.getMessage());
        }


    };
}
