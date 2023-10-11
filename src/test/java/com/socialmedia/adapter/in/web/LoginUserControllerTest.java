package com.socialmedia.adapter.in.web;

import com.socialmedia.adapter.in.web.vms.LoginUserVM;
import com.socialmedia.application.domain.services.LoginUserService;
import com.socialmedia.application.port.in.LoginUserCommand;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginUserControllerTest {
    private LoginUserController sut;

    @Mock
    private LoginUserService service;
    @Mock
    private Context ctx;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new LoginUserController(service);
    }

    @Test
    void loginExistingUser_whenValidInputAndServiceReturnsToken_shouldReturn200() throws Exception {
        // Given
        String email = "test@test.com";
        String password = "12345678";
        LoginUserVM loginUserVM = new LoginUserVM(email, password);
        LoginUserCommand command = new LoginUserCommand(email, password);

        when(ctx.bodyAsClass(LoginUserVM.class)).thenReturn(loginUserVM);
        String token = "testToken";
        when(service.loginUser(command)).thenReturn(token);
        when(ctx.status(200)).thenReturn(ctx);

        // When
        sut.loginExistingUser.handle(ctx);

        // Then
        verify(ctx).status(200);
        verify(ctx).result(token);
    }

    @Test
    void loginExistingUser_whenInvalidInput_shouldReturn400() throws Exception {
        // Given
        String email = "test@test.com";
        String password = null;
        LoginUserVM loginUserVM = new LoginUserVM(email, password);

        when(ctx.bodyAsClass(LoginUserVM.class)).thenReturn(loginUserVM);
        when(ctx.status(400)).thenReturn(ctx);

        // When
        sut.loginExistingUser.handle(ctx);

        // Then
        verify(ctx).status(400);
        verify(ctx).result("password: must not be null");
    }

    @Test
    void loginExistingUser_whenValidInputAndServiceReturnsNull_shouldReturn400() throws Exception {
        // Given
        String email = "test@test.com";
        String password = "12345678";
        LoginUserVM loginUserVM = new LoginUserVM(email, password);
        LoginUserCommand command = new LoginUserCommand(email, password);

        when(ctx.bodyAsClass(LoginUserVM.class)).thenReturn(loginUserVM);
        String token = null;
        when(service.loginUser(command)).thenReturn(token);
        when(ctx.status(401)).thenReturn(ctx);

        // When
        sut.loginExistingUser.handle(ctx);

        // Then
        verify(ctx).status(401);
        verify(ctx).result("User login failed.");
    }
}
