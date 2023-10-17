package com.socialmedia;

import com.socialmedia.accounts.adapter.out.CreateUserAdapter;
import com.socialmedia.accounts.adapter.out.LoadRoleAdapter;
import com.socialmedia.accounts.adapter.out.LoadUserAdapter;
import com.socialmedia.accounts.adapter.out.VerifyUserAdapter;
import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.application.port.in.LoginUserUseCase;
import com.socialmedia.accounts.application.port.in.VerifyUserUseCase;
import com.socialmedia.content.adapter.out.CreateCommentAdapter;
import com.socialmedia.content.adapter.out.CreatePostAdapter;
import com.socialmedia.content.adapter.out.LoadCommentAdapter;
import com.socialmedia.content.adapter.out.LoadPostAdapter;
import com.socialmedia.content.application.port.in.CreateCommentUseCase;
import com.socialmedia.content.application.port.in.CreatePostUseCase;
import com.socialmedia.content.application.port.out.CreateCommentPort;
import com.socialmedia.content.application.port.out.CreatePostPort;
import com.socialmedia.content.application.port.out.LoadCommentPort;
import com.socialmedia.content.application.port.out.LoadPostPort;
import com.socialmedia.content.application.services.CreateCommentService;
import com.socialmedia.content.application.services.CreatePostService;
import com.socialmedia.utils.authentication.exceptions.ExceptionHandler;
import com.socialmedia.accounts.adapter.in.UserController;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.application.services.LoginUserService;
import com.socialmedia.accounts.application.services.VerifyUserService;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.application.port.out.VerifyUserPort;
import com.socialmedia.content.adapter.in.ContentController;
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
        LoadPostPort loadPostPort = new LoadPostAdapter();
        LoadCommentPort loadCommentPort = new LoadCommentAdapter();
        CreateCommentPort createCommentPort = new CreateCommentAdapter();
        // Initialize services
        CreateUserUseCase createUserUseCase = new CreateUserService(createUserPort, loadUserPort);
        VerifyUserUseCase verifyUserUseCase = new VerifyUserService(loadUserPort, verifyUserPort);
        LoginUserUseCase loginUserUseCase = new LoginUserService(loadUserPort);
        CreatePostUseCase createPostUseCase = new CreatePostService(loadUserPort, loadRolePort, createPostPort);
        CreateCommentUseCase createCommentUseCase = new CreateCommentService(loadUserPort, loadRolePort, loadPostPort, loadCommentPort, createCommentPort);
        // Initialize controllers
        UserController userController = new UserController(
                createUserUseCase,
                verifyUserUseCase,
                loginUserUseCase);
        ContentController contentController = new ContentController(createPostUseCase, createCommentUseCase);

        // Define routes
        app.post("users", userController.createNewUser);
        app.post("users/verify/{email}", userController.verifyExistingUser);
        app.get("users/login", userController.loginExistingUser);
        app.post("posts", contentController.createNewPost);
        app.post("comments", contentController.createNewComment);

        // Start the server
        app.start(8080);
    }
}