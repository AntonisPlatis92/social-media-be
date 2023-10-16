package com.socialmedia.accounts.domain;

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
    private Long roleId;
    private Instant creationTime;
}
