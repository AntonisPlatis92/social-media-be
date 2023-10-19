package unit.com.socialmedia.utils.authentication;

import com.socialmedia.utils.authentication.JwtUtils;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    @Test
    void createToken_whenExtractingEmailFromToken_shouldMatchEmail() {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        String token = JwtUtils.createToken(userId);

        // Then
        assertNotNull(token);
        assertTrue(JwtUtils.isTokenValid(token));
        assertEquals(userId, JwtUtils.extractUserIdFromToken(token));
    }
}
