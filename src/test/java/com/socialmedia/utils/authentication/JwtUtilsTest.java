package com.socialmedia.utils.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    @Test
    void createToken_whenExtractingEmailFromToken_shouldMatchEmail() {
        // Given
        String email = "test@test.com";

        // When
        String token = JwtUtils.createToken(email);

        // Then
        assertNotNull(token);
        assertTrue(JwtUtils.isTokenValid(token));
        assertEquals(email, JwtUtils.extractEmailFromToken(token));
    }
}
