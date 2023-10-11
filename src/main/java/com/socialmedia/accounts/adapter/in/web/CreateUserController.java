package com.socialmedia.accounts.adapter.in.web;

import com.socialmedia.accounts.adapter.in.web.vms.CreateUserVM;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.application.exceptions.PasswordMinimumCharactersException;
import com.socialmedia.accounts.application.exceptions.UserAlreadyCreatedException;
import com.socialmedia.accounts.application.port.in.CreateUserCommand;
import io.javalin.http.Handler;
import jakarta.validation.ConstraintViolationException;

public class CreateUserController {
    private CreateUserService service;

    public CreateUserController(CreateUserService service) {
        this.service = service;
    }

    public Handler createNewUser = ctx -> {
        try {
            CreateUserVM createUserVM = ctx.bodyAsClass(CreateUserVM.class);
            if (createUserVM == null) {
                ctx.status(400).result("Invalid request body");
            }

            // Call the CreateUserService to create the user
            CreateUserCommand command = new CreateUserCommand(
                    createUserVM.email(),
                    createUserVM.password(),
                    createUserVM.roleId()
            );
            boolean userCreated = service.createUser(command);

            if (userCreated) {
                ctx.status(201).result("User created successfully.");
            } else {
                ctx.status(500).result("Failed to create user.");
            }
        }
        catch (PasswordMinimumCharactersException |
               UserAlreadyCreatedException  |
               ConstraintViolationException e) {
            ctx.status(400).result(e.getMessage());
        }


    };
}