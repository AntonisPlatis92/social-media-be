package com.socialmedia.accounts.application.port.out;

import com.socialmedia.accounts.domain.Role;

import java.util.Optional;

public interface LoadRolePort {
    Optional<Role> loadRoleById(Long roleId);
}
