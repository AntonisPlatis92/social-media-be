package com.socialmedia.accounts.adapter.in.web;

import com.socialmedia.accounts.adapter.in.web.vms.CreateUserVM;
import com.socialmedia.accounts.adapter.in.web.vms.LoginUserVM;
import com.socialmedia.accounts.application.port.in.CreateUserUseCase;
import com.socialmedia.accounts.application.port.in.LoginUserUseCase;
import com.socialmedia.accounts.application.port.in.VerifyUserUseCase;
import com.socialmedia.accounts.domain.commands.LoginUserCommand;
import com.socialmedia.accounts.domain.commands.VerifyUserCommand;
import com.socialmedia.accounts.application.services.LoginUserService;
import com.socialmedia.accounts.application.services.VerifyUserService;
import com.socialmedia.accounts.domain.exceptions.LoginFailedException;
import com.socialmedia.accounts.domain.exceptions.PasswordMinimumCharactersException;
import com.socialmedia.accounts.application.services.CreateUserService;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.accounts.domain.exceptions.UserAlreadyVerifiedException;
import io.javalin.http.Context;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserController sut;

    @Mock
    private CreateUserUseCase createUserUseCase;
    @Mock
    private VerifyUserUseCase verifyUserUseCase;
    @Mock
    private LoginUserUseCase loginUserUseCase;
    @Mock
    private Context ctx;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new UserController(createUserUseCase, verifyUserUseCase, loginUserUseCase);
    }

    @Test
    void createNewUser_whenValidInputAndServiceReturnsTrue_shouldReturn201() throws Exception {
        // Given
        String email = "test@test.com";
        String password = "12345678";
        Long roleId = 1L;
        CreateUserVM createUserVM = new CreateUserVM(email, password, roleId);
        CreateUserCommand command = new CreateUserCommand(email, password, roleId);

        when(ctx.bodyAsClass(CreateUserVM.class)).thenReturn(createUserVM);
        doNothing().when(createUserUseCase).createUser(command);
        when(ctx.status(201)).thenReturn(ctx);

        // When
        sut.createNewUser.handle(ctx);

        // Then
        verify(ctx).status(201);
        verify(ctx).result("User created successfully.");
    }

    @Test
    void createNewUser_whenInvalidInput_shouldThrowException() {
        // Given
        String email = "test@test.com";
        String password = null;
        Long roleId = 1L;
        CreateUserVM createUserVM = new CreateUserVM(email, password, roleId);

        when(ctx.bodyAsClass(CreateUserVM.class)).thenReturn(createUserVM);

        // Then
        assertThrows(ConstraintViolationException.class, () -> sut.createNewUser.handle(ctx));
    }

    @Test
    void createNewUser_whenValidInputAndPasswordIsLessThanMinChars_shouldThrowException() {
        // Given
        String email = "test@test.com";
        String password = "1234";
        Long roleId = 1L;
        CreateUserVM createUserVM = new CreateUserVM(email, password, roleId);

        when(ctx.bodyAsClass(CreateUserVM.class)).thenReturn(createUserVM);
        when(ctx.status(400)).thenReturn(ctx);

        // Then
        assertThrows(PasswordMinimumCharactersException.class, () -> sut.createNewUser.handle(ctx));
    }

    @Test
    void verifyExistingUser_whenValidInputAndServiceDoesNotThrow_shouldReturn200() throws Exception {
        // Given
        String email = "test@test.com";
        VerifyUserCommand command = new VerifyUserCommand(email);

        when(ctx.pathParam("email")).thenReturn(email);
        doNothing().when(verifyUserUseCase).verifyUser(command);
        when(ctx.status(200)).thenReturn(ctx);

        // When
        sut.verifyExistingUser.handle(ctx);

        // Then
        verify(ctx).status(200);
        verify(ctx).result("User verified successfully.");
    }

    @Test
    void verifyExistingUser_whenInvalidInput_shouldThrowException() throws Exception {
        // Given
        String email = null;

        when(ctx.pathParam("email")).thenReturn(email);
        when(ctx.status(400)).thenReturn(ctx);

        // Then
        assertThrows(ConstraintViolationException.class, () -> sut.verifyExistingUser.handle(ctx));
    }

    @Test
    void verifyExistingUser_whenValidInputAndServiceThrowsException_shouldThrowException() throws Exception {
        // Given
        String email = "test@test.com";
        VerifyUserCommand command = new VerifyUserCommand(email);

        when(ctx.pathParam("email")).thenReturn(email);
        doThrow(UserAlreadyVerifiedException.class).when(verifyUserUseCase).verifyUser(command);
        when(ctx.status(400)).thenReturn(ctx);

        // Then
        assertThrows(UserAlreadyVerifiedException.class, () -> sut.verifyExistingUser.handle(ctx));
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
        when(loginUserUseCase.loginUser(command)).thenReturn(token);
        when(ctx.status(200)).thenReturn(ctx);

        // When
        sut.loginExistingUser.handle(ctx);

        // Then
        verify(ctx).status(200);
        verify(ctx).result(token);
    }

    @Test
    void loginExistingUser_whenInvalidInput_shouldThrowException() throws Exception {
        // Given
        String email = "test@test.com";
        String password = null;
        LoginUserVM loginUserVM = new LoginUserVM(email, password);

        when(ctx.bodyAsClass(LoginUserVM.class)).thenReturn(loginUserVM);
        when(ctx.status(400)).thenReturn(ctx);

        // Then
        assertThrows(ConstraintViolationException.class, () -> sut.loginExistingUser.handle(ctx));
    }

    @Test
    void loginExistingUser_whenValidInputAndServiceReturnsNull_shouldThrowException() throws Exception {
        // Given
        String email = "test@test.com";
        String password = "12345678";
        LoginUserVM loginUserVM = new LoginUserVM(email, password);
        LoginUserCommand command = new LoginUserCommand(email, password);

        when(ctx.bodyAsClass(LoginUserVM.class)).thenReturn(loginUserVM);
        doThrow(LoginFailedException.class).when(loginUserUseCase).loginUser(command);
        when(ctx.status(401)).thenReturn(ctx);

        // Then
        assertThrows(LoginFailedException.class, () -> sut.loginExistingUser.handle(ctx));
    }
}
