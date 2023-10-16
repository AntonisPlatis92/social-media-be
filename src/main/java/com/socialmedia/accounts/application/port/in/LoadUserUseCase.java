package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface LoadUserUseCase {
    public Optional<User> loadUserById(UUID userId);
    public Optional<User> loadUserByEmail(String email);
}
