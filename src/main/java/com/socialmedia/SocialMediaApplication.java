package com.socialmedia;

import com.socialmedia.controllers.CreateUserController;
import com.socialmedia.controllers.LoginUserController;
import com.socialmedia.controllers.VerifyUserController;
import com.socialmedia.repositories.UserRepository;
import com.socialmedia.services.CreateUserService;
import com.socialmedia.services.LoginUserService;
import com.socialmedia.services.VerifyUserService;
import io.javalin.Javalin;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class SocialMediaApplication {
    public static void main(String[] args) {
        // Create an instance of Javalin
        Javalin app = Javalin.create();

        // Configure Javalin

        // Initialize your services and controllers
        CreateUserController createUserController = new CreateUserController(new CreateUserService(new UserRepository()));
        VerifyUserController verifyUserController = new VerifyUserController(new VerifyUserService(new UserRepository()));
        LoginUserController loginUserController = new LoginUserController(new LoginUserService(new UserRepository()));



        // Define routes
        app.post("users", createUserController.createNewUser);
        app.post("users/verify/{email}", verifyUserController.verifyExistingUser);
        app.get("users/login", loginUserController.loginExistingUser);

        // Start the server
        app.start(7000);
    }
}