package unit.com.socialmedia.accounts.domain;

import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.config.ClockConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@With
@AllArgsConstructor
@Builder
public class UserBuilder {
    private UUID userId;
    private String email;
    private String hashedPassword;
    private boolean verified;
    private Role role;
    private Instant creationTime;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String EMAIL = "testEmail";
    private static final String HASHED_PASSWORD = "hashedPassword";
    private static final Role FREE_USER_ROLE = RoleBuilder.aFreeUserRoleBuilder().build();


    public static UserBuilder aRandomUserBuilder() {
        return UserBuilder.builder()
                .userId(USER_ID)
                .email(EMAIL)
                .hashedPassword(HASHED_PASSWORD)
                .verified(false)
                .role(FREE_USER_ROLE)
                .creationTime(Instant.now(ClockConfig.utcClock()))
                .build();
    }

    public User build() {
        return User.builder()
                .userId(userId)
                .email(email)
                .hashedPassword(hashedPassword)
                .verified(verified)
                .role(role)
                .creationTime(creationTime)
                .build();
    }
}
