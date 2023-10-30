package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.accounts.application.port.in.LoadFollowsUseCase;
import com.socialmedia.accounts.application.port.out.LoadFollowPort;
import com.socialmedia.utils.database.JpaDatabaseUtils;

import java.util.List;
import java.util.UUID;

public class LoadFollowsService implements LoadFollowsUseCase {
    LoadFollowPort loadFollowPort;

    public LoadFollowsService(LoadFollowPort loadFollowPort) {
        this.loadFollowPort = loadFollowPort;
    }

    @Override
    public FollowsReturnVM loadFollowsByUserId(UUID userId) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> loadFollowPort.loadFollowsByUserId(userId));
    }

    @Override
    public List<UUID> loadFollowingUserIds(UUID userId) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> loadFollowPort.loadFollowingUserIdsByUserId(userId));
    }
}
