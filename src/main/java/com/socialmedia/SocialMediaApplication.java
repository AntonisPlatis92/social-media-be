package com.socialmedia;

import com.socialmedia.adapter.out.database.CreateUserAdapter;
import com.socialmedia.adapter.out.database.LoadUserAdapter;
import com.socialmedia.adapter.out.database.UserMapper;
import com.socialmedia.adapter.out.database.VerifyUserAdapter;
import com.socialmedia.adapter.in.web.CreateUserController;
import com.socialmedia.adapter.in.web.LoginUserController;
import com.socialmedia.adapter.in.web.VerifyUserController;
import com.socialmedia.application.domain.services.CreateUserService;
import com.socialmedia.application.domain.services.LoginUserService;
import com.socialmedia.application.domain.services.VerifyUserService;
import com.socialmedia.application.port.out.CreateUserPort;
import com.socialmedia.application.port.out.LoadUserPort;
import com.socialmedia.application.port.out.VerifyUserPort;
import io.javalin.Javalin;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class SocialMediaApplication {
    public static void main(String[] args) {
        // Create an instance of Javalin
        Javalin app = Javalin.create();

        // Configure Javalin

        // Initialize your services and controllers
        LoadUserPort loadUserPort = new LoadUserAdapter(new UserMapper());
        CreateUserPort createUserPort = new CreateUserAdapter();
        VerifyUserPort verifyUserPort = new VerifyUserAdapter();
        CreateUserController createUserController = new CreateUserController(new CreateUserService(createUserPort, loadUserPort));
        VerifyUserController verifyUserController = new VerifyUserController(new VerifyUserService(loadUserPort, verifyUserPort));
        LoginUserController loginUserController = new LoginUserController(new LoginUserService(loadUserPort));



        // Define routes
        app.post("users", createUserController.createNewUser);
        app.post("users/verify/{email}", verifyUserController.verifyExistingUser);
        app.get("users/login", loginUserController.loginExistingUser);

        // Start the server
        app.start(7000);
    }
}