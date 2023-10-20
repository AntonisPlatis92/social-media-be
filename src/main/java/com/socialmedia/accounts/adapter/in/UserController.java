package com.socialmedia.accounts.adapter.in;

import com.socialmedia.accounts.adapter.in.vms.CreateUserVM;
import com.socialmedia.accounts.adapter.in.vms.CreateFollowVM;
import com.socialmedia.accounts.adapter.in.vms.LoginUserVM;
import com.socialmedia.accounts.adapter.in.vms.RemoveFollowVM;
import com.socialmedia.accounts.application.port.in.*;
import com.socialmedia.accounts.domain.commands.*;
import com.socialmedia.utils.authentication.JwtUtils;
import io.javalin.http.Handler;

import java.util.UUID;

public class UserController {
    private CreateUserUseCase createUserUseCase;
    private VerifyUserUseCase verifyUserUseCase;
    private LoginUserUseCase loginUserUseCase;
    private CreateFollowUseCase createFollowUseCase;
    private RemoveFollowUseCase removeFollowUseCase;


    public UserController(
            CreateUserUseCase createUserUseCase,
            VerifyUserUseCase verifyUserUseCase,
            LoginUserUseCase loginUserUseCase,
            CreateFollowUseCase createFollowUseCase,
            RemoveFollowUseCase removeFollowUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.verifyUserUseCase = verifyUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.createFollowUseCase = createFollowUseCase;
        this.removeFollowUseCase = removeFollowUseCase;
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

        createUserUseCase.createUser(command);

        ctx.status(201).result("User created successfully.");
    };

    public Handler verifyExistingUser = ctx -> {
        String email = ctx.pathParam("email");

        VerifyUserCommand command = new VerifyUserCommand(email);
        verifyUserUseCase.verifyUser(command);

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
        String loginToken = loginUserUseCase.loginUser(command);

        ctx.status(200).result(loginToken);
    };

    public Handler followUser = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        CreateFollowVM createFollowVM = ctx.bodyAsClass(CreateFollowVM.class);
        if (createFollowVM == null) {
            ctx.status(400).result("Invalid request body");
        }

        CreateFollowCommand command = new CreateFollowCommand(
                userId,
                createFollowVM.followingUserEmail()
        );
        createFollowUseCase.createNewFollow(command);

        ctx.status(201).result("Follow created successfully.");
    };

    public Handler unfollowUser = ctx -> {
        String authorizationToken = ctx.header("Authorization");
        if (!JwtUtils.isTokenValid(authorizationToken)) {
            ctx.status(403).result("Token is invalid");
        }
        UUID userId = JwtUtils.extractUserIdFromToken(authorizationToken);

        RemoveFollowVM removeFollowVM = ctx.bodyAsClass(RemoveFollowVM.class);
        if (removeFollowVM == null) {
            ctx.status(400).result("Invalid request body");
        }

        RemoveFollowCommand command = new RemoveFollowCommand(
                userId,
                removeFollowVM.unfollowingUserEmail()
        );
        removeFollowUseCase.removeFollow(command);

        ctx.status(201).result("Follow removed successfully.");
    };
}