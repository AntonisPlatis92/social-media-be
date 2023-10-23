package com.socialmedia;

import com.socialmedia.accounts.adapter.in.UsersViewController;
import com.socialmedia.accounts.adapter.out.*;
import com.socialmedia.accounts.application.port.in.*;
import com.socialmedia.accounts.application.port.out.*;
import com.socialmedia.accounts.application.services.*;
import com.socialmedia.config.PropertiesManager;
import com.socialmedia.posts.adapter.in.PostViewController;
import com.socialmedia.posts.adapter.out.*;
import com.socialmedia.posts.application.memory.FollowingPostsMemory;
import com.socialmedia.posts.application.port.in.CreateCommentUseCase;
import com.socialmedia.posts.application.port.in.CreatePostUseCase;
import com.socialmedia.posts.application.port.in.FollowingPostsMemoryUseCase;
import com.socialmedia.posts.application.port.out.*;
import com.socialmedia.posts.application.services.CreateCommentService;
import com.socialmedia.posts.application.services.CreatePostService;
import com.socialmedia.posts.application.services.FollowingPostsMemoryService;
import com.socialmedia.utils.exceptions.ExceptionHandler;
import com.socialmedia.accounts.adapter.in.UserController;
import com.socialmedia.posts.adapter.in.PostController;
import com.socialmedia.posts.application.port.in.ViewPostsUseCase;
import com.socialmedia.posts.application.services.ViewPostsService;
import io.javalin.Javalin;
import org.flywaydb.core.Flyway;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class SocialMediaApplication {
    public static void main(String[] args) {
        System.setProperty("app.environment", "prod");

        // Create an instance of Javalin
        Javalin app = Javalin.create();

        // Configure Javalin
        ExceptionHandler.setupExceptionHandler(app);

        // Configure Flyway
        Flyway flyway = Flyway.configure()
                .locations("filesystem:sql/")
                .dataSource(PropertiesManager.getProperty("jdbc.url"), PropertiesManager.getProperty("jdbc.username"), PropertiesManager.getProperty("jdbc.password")).load();

        // Validate and run the migrations
        flyway.migrate();

        // Initialize memory
        FollowingPostsMemory followingPostsMemory = new FollowingPostsMemory();

        // Initialize ports
        LoadUserPort loadUserPort = new LoadUserAdapter();
        CreateUserPort createUserPort = new CreateUserAdapter();
        VerifyUserPort verifyUserPort = new VerifyUserAdapter();
        LoadRolePort loadRolePort = new LoadRoleAdapter();
        CreateFollowPort createFollowPort = new CreateFollowAdapter();
        RemoveFollowPort removeFollowPort = new RemoveFollowAdapter();
        CreatePostPort createPostPort = new CreatePostAdapter();
        LoadPostPort loadPostPort = new LoadPostAdapter();
        CreateCommentPort createCommentPort = new CreateCommentAdapter();
        LoadFollowingPostsPort loadFollowingPostsPort = new LoadFollowingPostsAdapter();
        LoadOwnPostsPort loadOwnPostsPort = new LoadOwnPostsAdapter();
        LoadCommentsOnOwnPostsPort loadCommentsOnOwnPostsPort = new LoadCommentsOnOwnPostsAdapter();
        LoadCommentsOnOwnAndFollowingPostsPort loadCommentsOnOwnAndFollowingPostsPort = new LoadCommentsOnOwnAndFollowingPostsAdapter();
        LoadFollowPort loadFollowPort = new LoadFollowAdapter();
        SearchUserPort searchUserPort = new SearchUserAdapter();
        // Initialize services
        LoadUserUseCase loadUserUseCase = new LoadUserService(loadUserPort);
        FollowingPostsMemoryUseCase followingPostsCacheUseCase = new FollowingPostsMemoryService(loadFollowingPostsPort, followingPostsMemory, loadUserUseCase);
        CreateUserUseCase createUserUseCase = new CreateUserService(loadUserPort, loadRolePort, createUserPort);
        VerifyUserUseCase verifyUserUseCase = new VerifyUserService(loadUserPort, verifyUserPort);
        LoginUserUseCase loginUserUseCase = new LoginUserService(loadUserPort);
        CreateFollowUseCase createFollowUseCase = new CreateFollowService(loadUserPort, createFollowPort, followingPostsCacheUseCase);
        RemoveFollowUseCase removeFollowUseCase = new RemoveFollowService(loadUserPort, removeFollowPort);
        CreatePostUseCase createPostUseCase = new CreatePostService(loadUserUseCase, createPostPort, followingPostsCacheUseCase);
        CreateCommentUseCase createCommentUseCase = new CreateCommentService(loadUserUseCase, loadPostPort, createCommentPort);
        LoadFollowsUseCase loadFollowsUseCase = new LoadFollowsService(loadFollowPort);
        ViewPostsUseCase viewFollowingPostsUseCase = new ViewPostsService(loadFollowingPostsPort, loadOwnPostsPort, loadCommentsOnOwnPostsPort, loadCommentsOnOwnAndFollowingPostsPort, loadFollowsUseCase, followingPostsMemory);
        SearchUsersUseCase searchUsersUseCase = new SearchUsersService(searchUserPort);
        // Initialize controllers
        UserController userController = new UserController(
                createUserUseCase,
                verifyUserUseCase,
                loginUserUseCase,
                createFollowUseCase,
                removeFollowUseCase);
        PostController postController = new PostController(createPostUseCase, createCommentUseCase);
        UsersViewController usersViewController = new UsersViewController(loadFollowsUseCase, searchUsersUseCase);
        PostViewController postViewController = new PostViewController(viewFollowingPostsUseCase);

        //
        followingPostsCacheUseCase.addUsersInFollowingPostsMemoryOnStartup();

        // Define routes
        app.post("users", userController.createNewUser);
        app.post("users/verify/{email}", userController.verifyExistingUser);
        app.get("users/login", userController.loginExistingUser);
        app.post("posts", postController.createNewPost);
        app.post("comments", postController.createNewComment);
        app.post("follows", userController.followUser);
        app.delete("follows", userController.unfollowUser);
        app.get("followingPosts", postViewController.viewFollowingPosts);
        app.get("ownPosts", postViewController.viewOwnPosts);
        app.get("commentsOnOwnPosts", postViewController.viewCommentsOnOwnPosts);
        app.get("commentsOnOwnAndFollowingPosts", postViewController.viewCommentsOnOwnAndFollowingPosts);
        app.get("ownFollows", usersViewController.viewOwnFollows);
        app.get("users/{term}", usersViewController.searchUsers);

        // Start the server
        app.start(8080);
    }
}