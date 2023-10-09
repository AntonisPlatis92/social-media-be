package com.socialmedia.controllers;

import com.socialmedia.controllers.vms.CreateUserVM;
import com.socialmedia.services.CreateUserService;
import com.socialmedia.utils.exceptions.PasswordMinimumCharactersException;
import com.socialmedia.utils.exceptions.UserAlreadyCreatedException;
import io.javalin.http.Handler;

public class CreateUserController {
    private CreateUserService service;

    public CreateUserController(CreateUserService service) {
        this.service = service;
    }

    public Handler createNewUser = ctx -> {
        try {
            var createUserVM = ctx.bodyAsClass(CreateUserVM.class);
            if (createUserVM == null) {
                ctx.status(400).result("Invalid request body");
            }

            // Call the CreateUserService to create the user
            boolean userCreated = service.createUser(
                    createUserVM.email(),
                    createUserVM.password(),
                    createUserVM.roleId()
            );

            if (userCreated) {
                ctx.status(201).result("User created successfully.");
            } else {
                ctx.status(500).result("Failed to create user.");
            }
        }
        catch (PasswordMinimumCharactersException | UserAlreadyCreatedException e) {
            ctx.status(400).result(e.getMessage());
        }


    };
}