package com.socialmedia.accounts.domain;

import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.utils.encoders.PasswordEncoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class User {
    private UUID userId;
    private String email;
    private String hashedPassword;
    private boolean verified;
    private Role role;
    private Instant creationTime;

    public static User createUserFromCommandAndRole(CreateUserCommand command, Role role){
        return new User(
                UUID.randomUUID(),
                command.email(),
                PasswordEncoder.encode(command.password()),
                false,
                role,
                Instant.now(ClockConfig.utcClock())
        );
    }
}
