package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadUserUseCase {
    Optional<User> loadUserById(UUID userId);
    Optional<User> loadUserByEmail(String email);
    List<User> loadUsersByFollowingMoreThan(Integer followingUsersThreshold);
}
