package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.utils.database.DatabaseUtils;

import java.util.Optional;
import java.util.UUID;

class LoadUserService implements LoadUserUseCase {
    private final LoadUserPort loadUserPort;

    public LoadUserService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    public Optional<User> loadUser(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> loadUserPort.loadUserById(userId));
    }
}
