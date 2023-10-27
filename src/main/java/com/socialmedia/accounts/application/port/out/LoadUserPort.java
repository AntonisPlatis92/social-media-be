package com.socialmedia.accounts.application.port.out;

import com.socialmedia.accounts.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadUserPort {
    Optional<User> loadUserByEmail(String email);
    Optional<User> loadUserById(UUID userId);
}
