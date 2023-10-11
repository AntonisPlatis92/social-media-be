package com.socialmedia.adapter.in.web;

import com.socialmedia.adapter.in.web.vms.CreateUserVM;
import com.socialmedia.application.domain.services.CreateUserService;
import com.socialmedia.application.port.in.CreateUserCommand;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.socialmedia.application.port.in.CreateUserCommand.MINIMUM_PASSWORD_CHARACTERS;
import static org.mockito.Mockito.*;

class CreateUserControllerTest {
    private CreateUserController sut;

    @Mock
    private CreateUserService service;
    @Mock
    private Context ctx;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new CreateUserController(service);
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
        when(service.createUser(command)).thenReturn(true);
        when(ctx.status(201)).thenReturn(ctx);

        // When
        sut.createNewUser.handle(ctx);

        // Then
        verify(ctx).status(201);
        verify(ctx).result("User created successfully.");
    }

    @Test
    void createNewUser_whenInvalidInput_shouldReturn400() throws Exception {
        // Given
        String email = "test@test.com";
        String password = null;
        Long roleId = 1L;
        CreateUserVM createUserVM = new CreateUserVM(email, password, roleId);

        when(ctx.bodyAsClass(CreateUserVM.class)).thenReturn(createUserVM);
        when(ctx.status(400)).thenReturn(ctx);

        // When
        sut.createNewUser.handle(ctx);

        // Then
        verify(ctx).status(400);
        verify(ctx).result("password: must not be null");
    }

    @Test
    void createNewUser_whenValidInputAndPasswordIsLessThanMinChars_shouldReturn400() throws Exception {
        // Given
        String email = "test@test.com";
        String password = "1234";
        Long roleId = 1L;
        CreateUserVM createUserVM = new CreateUserVM(email, password, roleId);

        when(ctx.bodyAsClass(CreateUserVM.class)).thenReturn(createUserVM);
        when(ctx.status(400)).thenReturn(ctx);

        // When
        sut.createNewUser.handle(ctx);

        // Then
        verify(ctx).status(400);
        verify(ctx).result(String.format("Password must be at least 8 characters long.", MINIMUM_PASSWORD_CHARACTERS));
    }
}
