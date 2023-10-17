package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.domain.Role;

import java.util.Optional;

public interface LoadRoleUseCase {
    Optional<Role> loadRole(Long roleId);
}
