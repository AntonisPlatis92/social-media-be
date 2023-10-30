package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.utils.database.JpaDatabaseUtils;

import java.util.Optional;
import java.util.UUID;

public class LoadUserService implements LoadUserUseCase {
    private final LoadUserPort loadUserPort;

    public LoadUserService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    public Optional<User> loadUserById(UUID userId) {
        return JpaDatabaseUtils.doInTransactionAndReturn((entityManager) -> loadUserPort.loadUserById(userId));
    }

    @Override
    public Optional<User> loadUserByEmail(String email) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> loadUserPort.loadUserByEmail(email));
    }
}
