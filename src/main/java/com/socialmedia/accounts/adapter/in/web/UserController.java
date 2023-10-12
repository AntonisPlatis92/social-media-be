package com.socialmedia.accounts.adapter.in.web;

import com.socialmedia.accounts.adapter.in.web.vms.CreateUserVM;
import com.socialmedia.accounts.adapter.in.web.vms.LoginUserVM;
import com.socialmedia.accounts.domain.commands.LoginUserCommand;
import com.socialmedia.accounts.domain.commands.VerifyUserCommand;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.accounts.application.services.LoginUserService;
import com.socialmedia.accounts.application.services.VerifyUserService;
import io.javalin.http.Handler;

public class UserController {
    private CreateUserService createUserService;
    private VerifyUserService verifyUserService;
    private LoginUserService loginUserService;


    public UserController(CreateUserService createUserService, VerifyUserService verifyUserService, LoginUserService loginUserService) {
        this.createUserService = createUserService;
        this.verifyUserService = verifyUserService;
        this.loginUserService = loginUserService;
    }

    public Handler createNewUser = ctx -> {
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

        createUserService.createUser(command);

        ctx.status(201).result("User created successfully.");
    };

    public Handler verifyExistingUser = ctx -> {
        String email = ctx.pathParam("email");

        VerifyUserCommand command = new VerifyUserCommand(email);
        verifyUserService.verifyUser(command);

        ctx.status(200).result("User verified successfully.");
    };

    public Handler loginExistingUser = ctx -> {
        LoginUserVM loginUserVM = ctx.bodyAsClass(LoginUserVM.class);
        if (loginUserVM == null) {
            ctx.status(400).result("Invalid request body");
        }

        LoginUserCommand command = new LoginUserCommand(
                loginUserVM.email(),
                loginUserVM.password()
        );
        String loginToken = loginUserService.loginUser(command);

        ctx.status(200).result(loginToken);
    };
}