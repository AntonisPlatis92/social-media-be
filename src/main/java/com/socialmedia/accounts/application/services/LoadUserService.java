package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.application.port.out.LoadUserPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoadUserService implements LoadUserUseCase {
    private final LoadUserPort loadUserPort;

    public LoadUserService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    public Optional<User> loadUserById(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUserById(userId));
    }

    @Override
    public Optional<User> loadUserByEmail(String email) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUserByEmail(email));
    }

    @Override
    public List<User> loadUsersByFollowingMoreThan(Integer followingUsersThreshold) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUsersByFollowingMoreThan(followingUsersThreshold));
    }
}
