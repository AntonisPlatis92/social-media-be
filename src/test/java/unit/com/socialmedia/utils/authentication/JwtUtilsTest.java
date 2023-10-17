package unit.com.socialmedia.utils.authentication;

import com.socialmedia.utils.authentication.JwtUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    @Test
    void createToken_whenExtractingEmailFromToken_shouldMatchEmail() {
        // Given
        String userEmail = "test@test.com";

        // When
        String token = JwtUtils.createToken(userEmail);

        // Then
        assertNotNull(token);
        assertTrue(JwtUtils.isTokenValid(token));
        assertEquals(userEmail, JwtUtils.extractUserEmailFromToken(token));
    }
}
