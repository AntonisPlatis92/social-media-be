package com.socialmedia.content.adapters.in;

import com.socialmedia.config.ClockConfig;
import com.socialmedia.content.adapter.in.ContentController;
import com.socialmedia.content.adapter.in.vms.CreatePostVM;
import com.socialmedia.content.application.port.in.CreatePostUseCase;
import com.socialmedia.content.domain.commands.CreatePostCommand;
import com.socialmedia.utils.authentication.JwtUtils;
import io.javalin.http.Context;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static com.socialmedia.utils.authentication.JwtUtils.SECRET_KEY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ContentControllerTest {
    private ContentController sut;

    @Mock
    private CreatePostUseCase createPostUseCase;
    @Mock
    private Context ctx;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new ContentController(createPostUseCase);
    }

    @Test
    void createNewPost_whenValidInputAndServiceReturnsTrue_shouldReturn201() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String token = JwtUtils.createToken(userId);
        when(ctx.header("Authorization")).thenReturn(token);
        String postBody = "testBody";
        CreatePostVM createPostVM = new CreatePostVM(postBody);
        CreatePostCommand command = new CreatePostCommand(userId, postBody);

        when(ctx.bodyAsClass(CreatePostVM.class)).thenReturn(createPostVM);
        doNothing().when(createPostUseCase).createPost(command);
        when(ctx.status(201)).thenReturn(ctx);

        // When
        sut.createNewPost.handle(ctx);

        // Then
        verify(ctx).status(201);
        verify(ctx).result("Post created successfully.");
    }

    @Test
    void createNewPost_whenInvalidInput_shouldThrowConstraintViolationException() {
        // Given
        UUID userId = UUID.randomUUID();
        String token = JwtUtils.createToken(userId);
        when(ctx.header("Authorization")).thenReturn(token);
        String postBody = null;
        CreatePostVM createPostVM = new CreatePostVM(postBody);

        when(ctx.bodyAsClass(CreatePostVM.class)).thenReturn(createPostVM);

        // Then
        assertThrows(ConstraintViolationException.class, () -> sut.createNewPost.handle(ctx));
    }

    @Test
    void createNewPost_whenInvalidToken_shouldThrowMalformedJwtException() {
        // Given
        String token = "token";
        when(ctx.header("Authorization")).thenReturn(token);
        String postBody = "postBody";
        CreatePostVM createPostVM = new CreatePostVM(postBody);

        when(ctx.bodyAsClass(CreatePostVM.class)).thenReturn(createPostVM);
        when(ctx.status(403)).thenReturn(ctx);

        // Then
        assertThrows(MalformedJwtException.class, () -> sut.createNewPost.handle(ctx));
    }

    @Test
    void createNewPost_whenExpiredToken_shouldThrowException() {
        // Given
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now(ClockConfig.utcClock());
        Instant expirationInPast = now.minus(1, ChronoUnit.DAYS);
        String expiredToken = Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationInPast))
                .signWith(SECRET_KEY)
                .compact();
        when(ctx.header("Authorization")).thenReturn(expiredToken);
        String postBody = "postBody";
        CreatePostVM createPostVM = new CreatePostVM(postBody);

        when(ctx.bodyAsClass(CreatePostVM.class)).thenReturn(createPostVM);
        when(ctx.status(403)).thenReturn(ctx);

        // Then
        assertThrows(ExpiredJwtException.class, () -> sut.createNewPost.handle(ctx));
    }
}
