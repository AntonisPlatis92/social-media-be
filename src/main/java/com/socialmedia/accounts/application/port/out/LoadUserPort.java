package com.socialmedia.accounts.application.port.out;

import com.socialmedia.accounts.domain.User;

import java.util.Optional;

public interface LoadUserPort {
    Optional<User> loadUser(String email);
}
