package com.socialmedia.accounts.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class User {
    private String email;
    private String hashedPassword;
    private boolean verified;
    private Long roleId;
    private Instant creationTime;
}
