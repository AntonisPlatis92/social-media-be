package com.socialmedia.adapter.in.web;

import com.socialmedia.application.domain.services.VerifyUserService;
import com.socialmedia.application.domain.utils.exceptions.UserAlreadyVerifiedException;
import com.socialmedia.application.port.in.VerifyUserCommand;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class VerifyUserControllerTest {
    private VerifyUserController sut;

    @Mock
    private VerifyUserService service;
    @Mock
    private Context ctx;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new VerifyUserController(service);
    }

    @Test
    void verifyExistingUser_whenValidInputAndServiceDoesNotThrow_shouldReturn200() throws Exception {
        // Given
        String email = "test@test.com";
        VerifyUserCommand command = new VerifyUserCommand(email);

        when(ctx.pathParam("email")).thenReturn(email);
        doNothing().when(service).verifyUser(command);
        when(ctx.status(200)).thenReturn(ctx);

        // When
        sut.verifyExistingUser.handle(ctx);

        // Then
        verify(ctx).status(200);
        verify(ctx).result("User verified successfully.");
    }

    @Test
    void verifyExistingUser_whenInvalidInput_shouldReturn400() throws Exception {
        // Given
        String email = null;

        when(ctx.pathParam("email")).thenReturn(email);
        when(ctx.status(400)).thenReturn(ctx);

        // When
        sut.verifyExistingUser.handle(ctx);

        // Then
        verify(ctx).status(400);
        verify(ctx).result("email: must not be null");
    }

    @Test
    void verifyExistingUser_whenValidInputAndServiceThrowsException_shouldReturn200() throws Exception {
        // Given
        String email = "test@test.com";
        VerifyUserCommand command = new VerifyUserCommand(email);

        when(ctx.pathParam("email")).thenReturn(email);
        doThrow(UserAlreadyVerifiedException.class).when(service).verifyUser(command);
        when(ctx.status(400)).thenReturn(ctx);

        // When
        sut.verifyExistingUser.handle(ctx);

        // Then
        verify(ctx).status(400);
    }
}
