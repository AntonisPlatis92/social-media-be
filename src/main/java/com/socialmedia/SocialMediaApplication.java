package com.socialmedia;

import com.socialmedia.accounts.adapter.out.*;
import com.socialmedia.accounts.application.port.in.*;
import com.socialmedia.accounts.application.port.out.*;
import com.socialmedia.accounts.application.services.*;
import com.socialmedia.posts.adapter.in.PostViewController;
import com.socialmedia.posts.adapter.out.*;
import com.socialmedia.posts.application.port.in.CreateCommentUseCase;
import com.socialmedia.posts.application.port.in.CreatePostUseCase;
import com.socialmedia.posts.application.port.in.LoadPostUseCase;
import com.socialmedia.posts.application.port.out.*;
import com.socialmedia.posts.application.services.CreateCommentService;
import com.socialmedia.posts.application.services.CreatePostService;
import com.socialmedia.posts.application.services.LoadPostService;
import com.socialmedia.utils.exceptions.ExceptionHandler;
import com.socialmedia.accounts.adapter.in.UserController;
import com.socialmedia.posts.adapter.in.PostController;
import com.socialmedia.views.adapter.in.ViewController;
import com.socialmedia.views.application.port.in.ViewFollowsUseCase;
import com.socialmedia.posts.application.port.in.ViewPostsUseCase;
import com.socialmedia.views.application.services.ViewFollowsService;
import com.socialmedia.posts.application.services.ViewPostsService;
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
        CreateFollowPort createFollowPort = new CreateFollowAdapter();
        CreatePostPort createPostPort = new CreatePostAdapter();
        LoadPostPort loadPostPort = new LoadPostAdapter();
        CreateCommentPort createCommentPort = new CreateCommentAdapter();
        LoadFollowingPostsPort loadFollowingPostsPort = new LoadFollowingPostsAdapter();
        LoadOwnPostsPort loadOwnPostsPort = new LoadOwnPostsAdapter();
        // Initialize services
        CreateUserUseCase createUserUseCase = new CreateUserService(loadUserPort, loadRolePort, createUserPort);
        VerifyUserUseCase verifyUserUseCase = new VerifyUserService(loadUserPort, verifyUserPort);
        LoginUserUseCase loginUserUseCase = new LoginUserService(loadUserPort);
        CreateFollowUseCase createFollowUseCase = new CreateFollowService(loadUserPort, createFollowPort);
        LoadUserUseCase loadUserUseCase = new LoadUserService(loadUserPort);
        CreatePostUseCase createPostUseCase = new CreatePostService(loadUserUseCase, createPostPort);
        CreateCommentUseCase createCommentUseCase = new CreateCommentService(loadUserUseCase, loadPostPort, createCommentPort);
        LoadPostUseCase loadPostUseCase = new LoadPostService(loadPostPort);
        ViewPostsUseCase viewFollowingPostsUseCase = new ViewPostsService(loadUserUseCase, loadPostUseCase, loadFollowingPostsPort, loadOwnPostsPort);
        ViewFollowsUseCase viewFollowsUseCase = new ViewFollowsService(loadUserUseCase);
        // Initialize controllers
        UserController userController = new UserController(
                createUserUseCase,
                verifyUserUseCase,
                loginUserUseCase,
                createFollowUseCase);
        PostController postController = new PostController(createPostUseCase, createCommentUseCase);
        ViewController viewController = new ViewController(viewFollowingPostsUseCase, viewFollowsUseCase);
        PostViewController postViewController = new PostViewController(viewFollowingPostsUseCase);

        // Define routes
        app.post("users", userController.createNewUser);
        app.post("users/verify/{email}", userController.verifyExistingUser);
        app.get("users/login", userController.loginExistingUser);
        app.post("posts", postController.createNewPost);
        app.post("comments", postController.createNewComment);
        app.post("follows", userController.followUser);
        app.get("followingPosts", postViewController.viewFollowingPosts);
        app.get("ownPosts", postViewController.viewOwnPosts);
        app.get("commentsOnOwnPosts", viewController.viewCommentsOnOwnPosts);
        app.get("commentsOnOwnAndFollowingPosts", viewController.viewCommentsOnOwnAndFollowingPosts);
        app.get("ownFollows", viewController.viewOwnFollows);

        // Start the server
        app.start(8080);
    }
}