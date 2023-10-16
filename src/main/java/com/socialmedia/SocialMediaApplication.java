package com.socialmedia;

import com.socialmedia.accounts.adapter.out.database.CreateUserAdapter;
import com.socialmedia.accounts.adapter.out.database.LoadRoleAdapter;
import com.socialmedia.accounts.adapter.out.database.LoadUserAdapter;
import com.socialmedia.accounts.adapter.out.database.VerifyUserAdapter;
import com.socialmedia.accounts.adapter.in.web.UserController;
import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.application.port.in.LoginUserUseCase;
import com.socialmedia.accounts.application.port.in.VerifyUserUseCase;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.application.services.LoginUserService;
import com.socialmedia.accounts.application.services.VerifyUserService;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.out.VerifyUserPort;
import com.socialmedia.content.adapter.in.ContentController;
import com.socialmedia.content.adapter.out.CreatePostAdapter;
import com.socialmedia.content.application.port.in.CreatePostUseCase;
import com.socialmedia.content.application.port.out.CreatePostPort;
import com.socialmedia.content.application.services.CreatePostService;
import com.socialmedia.utils.authentication.exceptions.ExceptionHandler;
import io.javalin.Javalin;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class SocialMediaApplication {
    public static void main(String[] args) {
        System.setProperty("app.environment", "prod");

        // Create an instance of Javalin
        Javalin app = Javalin.create();

        // Configure Javalin
        ExceptionHandler.setupExceptionHandler(app);

        // Initialize ports
        LoadUserPort loadUserPort = new LoadUserAdapter();
        CreateUserPort createUserPort = new CreateUserAdapter();
        VerifyUserPort verifyUserPort = new VerifyUserAdapter();
        LoadRolePort loadRolePort = new LoadRoleAdapter();
        CreatePostPort createPostPort = new CreatePostAdapter();
        // Initialize services
        CreateUserUseCase createUserUseCase = new CreateUserService(createUserPort, loadUserPort);
        VerifyUserUseCase verifyUserUseCase = new VerifyUserService(loadUserPort, verifyUserPort);
        LoginUserUseCase loginUserUseCase = new LoginUserService(loadUserPort);
        CreatePostUseCase createPostUseCase = new CreatePostService(loadUserPort, loadRolePort, createPostPort);
        // Initialize controllers
        UserController userController = new UserController(
                createUserUseCase,
                verifyUserUseCase,
                loginUserUseCase);
        ContentController contentController = new ContentController(createPostUseCase);

        // Define routes
        app.post("users", userController.createNewUser);
        app.post("users/verify/{email}", userController.verifyExistingUser);
        app.get("users/login", userController.loginExistingUser);
        app.post("posts", contentController.createNewPost);

        // Start the server
        app.start(7000);
    }
}